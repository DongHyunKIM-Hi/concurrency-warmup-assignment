#!/usr/bin/env bash
# 시나리오 자동 확인 (S1~S11)
#
# 사용법
#   1) 구현한 서버 실행:   ./gradlew bootRun        (8080)
#   2) bash check-scenarios.sh
#
# - 실행할 때마다 새 ID(회의실·사용자)를 쓰므로, 서버를 재시작하지 않고 몇 번이든 돌릴 수 있습니다.
# - 외부 서버나 Docker는 필요하지 않습니다.
# - 동시 요청 중 "서로 기다리지 않아야 통과"인 시나리오(S5·S10)는 타이밍에 따라
#   흔들릴 수 있어 최대 3번 재시도한 뒤 판정합니다.
# - "동시에 와도 1건만 성공해야 통과"인 시나리오(S2·S3·S8)는 재시도하지 않습니다.
#   락이 없으면 우연히 1건만 성공하는 회차가 나올 수 있는데, 재시도로 그 회차를
#   집으면 깨진 코드가 통과로 보이기 때문입니다.
#
# - 윈도우(Git Bash 등)에서 curl이 줄 끝에 캐리지리턴(\r)을 붙여도 비교가 깨지지 않도록
#   응답을 받는 곳에서 \r을 제거합니다.
#
# 다른 포트로 띄운 서버를 확인하려면:
#   SERVER_URL=http://localhost:8081 bash check-scenarios.sh

S=${SERVER_URL:-http://localhost:8080}
H='Content-Type: application/json'

res1=""; res2=""; res3=""; res4=""; res5=""; res6=""
res7=""; res8=""; res9=""; res10=""; res11=""
PASS=0; FAIL=0

ok(){ eval "res$1=\"\$(printf '\033[32m[통과]\033[0m S%-3s %s' \"$1\" \"$2\")\""; PASS=$((PASS+1)); }
no(){ eval "res$1=\"\$(printf '\033[31m[실패]\033[0m S%-3s %s — %s' \"$1\" \"$2\" \"$3\")\""; FAIL=$((FAIL+1)); }

code(){ curl -s -o /dev/null -w '%{http_code}' --max-time 15 "$@" | tr -d '\r'; }
# 쿠폰 발급: 상태 코드만 반환
issue(){ curl -s -o /dev/null -w '%{http_code}' --max-time 15 -X POST "$S/api/users/$1/coupons" -H "$H" -d "{\"type\":\"$2\"}" | tr -d '\r'; }
# 예약 생성: 상태 코드 + 줄바꿈 (동시 호출 결과 수집용)
reserve(){ curl -s -o /dev/null -w '%{http_code}\n' --max-time 15 -X POST "$S/api/reservations" -H "$H" \
  -d "{\"roomId\":\"$1\",\"userId\":\"$2\",\"from\":\"$3\",\"to\":\"$4\"}" | tr -d '\r'; }
# JSON 필드 값 추출 (문자열/숫자 모두)
field(){ printf '%s' "$1" | tr -d '\r' | sed -n "s/.*\"$2\":\"\{0,1\}\([^\",}]*\)\"\{0,1\}.*/\1/p" | head -1; }
# 패턴 등장 횟수
cnt(){ printf '%s\n' "$1" | tr -d '\r' | grep -o "$2" | wc -l | tr -d ' '; }
# 줄 목록에서 특정 코드 개수
ncode(){ printf '%s\n' "$1" | tr -d '\r' | grep -c "^$2\$"; }

echo "서버($S) 연결을 확인합니다..."
[ "$(code "$S/api/reservations/rooms/PING-0")" = "000" ] && {
  echo "서버에 연결할 수 없습니다. ./gradlew bootRun 후 다시 실행해 주세요."; exit 1; }

R=$RANDOM$RANDOM
echo "시나리오를 확인합니다..."

# ====================== 쿠폰 (S1~S5) ======================

# ---- S1: 쿠폰 발급 후 사용 → 가장 큰 할인 쿠폰 적용·삭제 ----
U1="s1-$R"
issue "$U1" WON_800 >/dev/null
issue "$U1" PERCENT_15 >/dev/null
r=$(curl -s -w '\n%{http_code}' --max-time 15 -X POST "$S/api/users/$U1/coupons/use" -H "$H" -d '{"amount":10000}' | tr -d '\r')
c=${r##*$'\n'}; b=${r%$'\n'*}
ct=$(field "$b" couponType); da=$(field "$b" discountAmount); fa=$(field "$b" finalAmount)
rem=$(curl -s --max-time 15 "$S/api/users/$U1/coupons")
remn=$(cnt "$rem" '"id"')
if [ "$c" = "200" ] && [ "$ct" = "PERCENT_15" ] && [ "$da" = "1500" ] && [ "$fa" = "8500" ] \
   && [ "$remn" = "1" ] && printf '%s' "$rem" | grep -q 'WON_800'; then
  ok 1 "정상 사용 → 가장 큰 할인 쿠폰(PERCENT_15) 적용·삭제, WON_800 남음"
else
  no 1 "정상 사용 (가장 큰 할인 적용·삭제)" "응답 $c, 쿠폰 ${ct:-없음}, 할인 ${da:-?}, 최종 ${fa:-?}, 남은 ${remn}장"
fi

# ---- S2: 쿠폰 2장, 동시 사용 2건 → 둘 다 200, 서로 다른 쿠폰 1장씩, 이후 0장 ----
att=0; done2=0; detail2=""
while [ $att -lt 1 ]; do   # 중복 사용 방지 확인이라 재시도하지 않습니다
  att=$((att+1)); U="s2-$R-$att"
  issue "$U" PERCENT_15 >/dev/null; issue "$U" WON_800 >/dev/null
  types=$( ( for i in 1 2; do curl -s --max-time 15 -X POST "$S/api/users/$U/coupons/use" -H "$H" \
            -d '{"amount":10000}' | tr -d '\r' | grep -o '"couponType":"[A-Z_0-9]*"' & done; wait ) )
  got=$(cnt "$types" 'couponType'); uniq=$(printf '%s\n' "$types" | sort -u | grep -c 'couponType')
  remn=$(cnt "$(curl -s --max-time 15 "$S/api/users/$U/coupons")" '"id"')
  if [ "$got" = "2" ] && [ "$uniq" = "2" ] && [ "$remn" = "0" ]; then done2=1; break; fi
  detail2="성공 ${got}건(기대 2), 서로 다른 쿠폰 ${uniq}종(기대 2), 남은 ${remn}장(기대 0)"
  sleep 0.3
done
[ $done2 = 1 ] && ok 2 "쿠폰 2장 동시 2건 → 둘 다 200, 서로 다른 쿠폰 1장씩" \
             || no 2 "쿠폰 2장 동시 2건 (둘 다 200, 다른 쿠폰 1장씩)" "$detail2"

# ---- S3: 쿠폰 1장, 동시 사용 5건 → 200 1건, 404 4건 ----
att=0; done3=0; detail3=""
while [ $att -lt 1 ]; do   # 중복 사용 방지 확인이라 재시도하지 않습니다
  att=$((att+1)); U="s3-$R-$att"
  issue "$U" PERCENT_15 >/dev/null
  codes=$( ( for i in 1 2 3 4 5; do curl -s -o /dev/null -w '%{http_code}\n' --max-time 15 \
            -X POST "$S/api/users/$U/coupons/use" -H "$H" -d '{"amount":10000}' | tr -d '\r' & done; wait ) )
  n200=$(ncode "$codes" 200); n404=$(ncode "$codes" 404)
  if [ "$n200" = "1" ] && [ "$n404" = "4" ]; then done3=1; break; fi
  detail3="200 ${n200}건(기대 1), 404 ${n404}건(기대 4)"
  sleep 0.3
done
[ $done3 = 1 ] && ok 3 "쿠폰 1장 동시 5건 → 200 1건, 404 4건 (중복 사용 없음)" \
             || no 3 "쿠폰 1장 동시 5건 (200 1건, 404 4건)" "$detail3"

# ---- S4: 쿠폰 없음 → 404 NO_COUPON_AVAILABLE ----
r=$(curl -s -w '\n%{http_code}' --max-time 15 -X POST "$S/api/users/nobody-$R/coupons/use" -H "$H" -d '{"amount":1000}' | tr -d '\r')
c=${r##*$'\n'}; b=${r%$'\n'*}; ec=$(field "$b" code)
if [ "$c" = "404" ] && [ "$ec" = "NO_COUPON_AVAILABLE" ]; then
  ok 4 "쿠폰 없을 때 사용 → 404 NO_COUPON_AVAILABLE"
else
  no 4 "쿠폰 없을 때 사용 (404 NO_COUPON_AVAILABLE)" "응답 $c, 코드 ${ec:-없음}"
fi

# ---- S5: 서로 다른 사용자 동시 사용 → 둘 다 200 ----
att=0; done5=0; detail5=""
while [ $att -lt 3 ]; do
  att=$((att+1)); UA="s5a-$R-$att"; UB="s5b-$R-$att"
  issue "$UA" PERCENT_15 >/dev/null; issue "$UB" PERCENT_15 >/dev/null
  codes=$( ( for u in "$UA" "$UB"; do curl -s -o /dev/null -w '%{http_code}\n' --max-time 15 \
            -X POST "$S/api/users/$u/coupons/use" -H "$H" -d '{"amount":10000}' | tr -d '\r' & done; wait ) )
  n200=$(ncode "$codes" 200)
  if [ "$n200" = "2" ]; then done5=1; break; fi
  detail5="200 ${n200}건(기대 2)"
  sleep 0.3
done
[ $done5 = 1 ] && ok 5 "다른 사용자 동시 사용 → 둘 다 200" \
             || no 5 "다른 사용자 동시 사용 (둘 다 200)" "$detail5 (${att}회 시도)"

# ====================== 회의실 예약 (S6~S11) ======================

# ---- S6: 정상 예약 → 201 / S7: 같은 회의실 겹치는 예약(순차) → 409 ----
ROOM67="r67-$R"
c6=$(reserve "$ROOM67" U1 10:00 11:00)
[ "$c6" = "201" ] && ok 6 "정상 예약 → 201" || no 6 "정상 예약 (201)" "응답 $c6"

c7=$(reserve "$ROOM67" U2 10:30 11:30)
[ "$c7" = "409" ] && ok 7 "같은 회의실 겹치는 예약(순차) → 409 TIME_CONFLICT" \
                 || no 7 "같은 회의실 겹치는 예약(순차) (409)" "응답 $c7"

# ---- S8: 같은 회의실, 같은 시간 동시 5건 → 201 1건, 409 4건 ----
att=0; done8=0; detail8=""
while [ $att -lt 1 ]; do   # 겹침 방지 확인이라 재시도하지 않습니다
  att=$((att+1)); ROOM="r8-$R-$att"
  codes=$( ( for i in 1 2 3 4 5; do reserve "$ROOM" "U$i" 09:00 10:00 & done; wait ) )
  n201=$(ncode "$codes" 201); n409=$(ncode "$codes" 409)
  saved=$(cnt "$(curl -s --max-time 15 "$S/api/reservations/rooms/$ROOM")" '"userId"')
  if [ "$n201" = "1" ] && [ "$n409" = "4" ] && [ "$saved" = "1" ]; then done8=1; break; fi
  detail8="201 ${n201}건(기대 1), 409 ${n409}건(기대 4), 저장 ${saved}건(기대 1)"
  sleep 0.3
done
[ $done8 = 1 ] && ok 8 "같은 회의실 동시 5건 → 201 1건, 409 4건, 저장 1건" \
             || no 8 "같은 회의실 동시 5건 (201 1건, 나머지 409)" "$detail8"

# ---- S9: 끝과 시작만 맞닿은 시간 → 둘 다 201 ----
ROOM9="r9-$R"
c9a=$(reserve "$ROOM9" U1 10:00 11:00)
c9b=$(reserve "$ROOM9" U2 11:00 12:00)
if [ "$c9a" = "201" ] && [ "$c9b" = "201" ]; then
  ok 9 "맞닿은 시간(10~11, 11~12) → 둘 다 201"
else
  no 9 "맞닿은 시간 (둘 다 201)" "10~11 $c9a, 11~12 $c9b"
fi

# ---- S10: 다른 회의실, 같은 시간 동시 → 둘 다 201 ----
att=0; done10=0; detail10=""
while [ $att -lt 3 ]; do
  att=$((att+1)); N="$R-$att"
  codes=$( ( reserve "r10a-$N" UA 09:00 10:00 & reserve "r10b-$N" UB 09:00 10:00 & wait ) )
  n201=$(ncode "$codes" 201)
  if [ "$n201" = "2" ]; then done10=1; break; fi
  detail10="201 ${n201}건(기대 2)"
  sleep 0.3
done
[ $done10 = 1 ] && ok 10 "다른 회의실 같은 시간 동시 → 둘 다 201" \
              || no 10 "다른 회의실 같은 시간 동시 (둘 다 201)" "$detail10 (${att}회 시도)"

# ---- S11: 사용자별 예약 조회 → 해당 사용자 예약만 ----
UX="s11-$R"; OTHER="s11other-$R"
reserve "s11a-$R" "$UX"    08:00 09:00 >/dev/null
reserve "s11b-$R" "$UX"    08:00 09:00 >/dev/null
reserve "s11a-$R" "$OTHER" 09:00 10:00 >/dev/null   # 같은 회의실·다른 사용자 (겹치지 않음)
body=$(curl -s --max-time 15 "$S/api/reservations/users/$UX")
n=$(cnt "$body" '"roomId"')
if [ "$n" = "2" ] && printf '%s' "$body" | grep -q 's11a-' && printf '%s' "$body" | grep -q 's11b-'; then
  ok 11 "사용자별 조회 → 해당 사용자 예약 2건만(다른 사용자 제외)"
else
  no 11 "사용자별 조회 (해당 사용자 예약만)" "조회 ${n}건(기대 2)"
fi

# ====================== 결과 ======================
echo; echo "================ 시나리오 확인 결과 ================"
for i in 1 2 3 4 5 6 7 8 9 10 11; do eval "printf '%b\n' \"\$res$i\""; done
echo "===================================================="
echo "통과 $PASS / 11"
[ "$FAIL" -gt 0 ] && echo "실패한 시나리오는 README의 시나리오 목록(7장)에서 해당 항목을 확인해 주세요."
exit 0
