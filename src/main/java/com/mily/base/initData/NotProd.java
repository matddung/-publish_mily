package com.mily.base.initData;

import com.mily.article.feed.entity.Feed;
import com.mily.article.feed.service.FeedService;
import com.mily.article.milyx.MilyX;
import com.mily.article.milyx.MilyXService;
import com.mily.article.milyx.category.CategoryService;
import com.mily.article.milyx.category.entity.FirstCategory;
import com.mily.article.milyx.category.entity.SecondCategory;
import com.mily.article.milyx.comment.MilyXCommentService;
import com.mily.payment.PaymentService;
import com.mily.user.MilyUser;
import com.mily.user.MilyUserRepository;
import com.mily.user.MilyUserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Optional;

@Configuration
@AllArgsConstructor
@Profile("!prod")
public class NotProd {
    private final MilyUserService milyUserService;
    private final MilyUserRepository milyUserRepository;
    private final CategoryService categoryService;
    private final MilyXService milyXService;
    private final FeedService feedService;
    private final MilyXCommentService milyXCommentService;
    private final PaymentService paymentService;
    private FirstCategory fc;

    @Bean
    public ApplicationRunner init() {
        return args -> {
            Optional<MilyUser> mu = milyUserService.findByUserLoginId("admin999");
            if (mu.isEmpty()) {
                MilyUser milyUser1 = milyUserService.userSignup("admin999", "qweasdzxc", "administrator", "admin999@admin.com", "01099999999", "1975-01-21").getData();
                milyUser1.setRole("admin");
                milyUserRepository.save(milyUser1);
                MilyUser milyUser2 = milyUserService.userSignup("oizill5481", "a7586898", "이재준", "oizill5481@icloud.com", "01045702579", "1996-10-05").getData();
                milyUser2.setMilyPoint(2000);
                milyUserRepository.save(milyUser2);
                MilyUser milyUser3 = milyUserService.userSignup("leewowns1005", "a7586898", "이재준", "leewowns1005@naver.com", "01020105481", "1996-10-05").getData();
                milyUserService.lawyerSignup("교통사고/범죄", "교통 사고, 상해 전문 승소율 97% 이상 / 처리까지 최저 수임료로", "법무법인 아로", "1111-1111-1111", "대전", milyUser3, "https://i.postimg.cc/C5cfn9CN/Kakao-Talk-20231207-200746924.jpg");
                MilyUser milyUser4 = milyUserService.userSignup("test2222", "test2222", "이은현", "test2222@email.com", "01022222222", "1999-04-08").getData();
                milyUserService.lawyerSignup("민사 절차", "<1,400건 이상의 후기> 검증된 변호사 / 합리적 수임료", "법무법인 새긴", "2222-2222-2222", "울산", milyUser4, "https://i.postimg.cc/63sjzFqt/SE-8dd832e9-2fa4-4cb4-9868-f1ebc.jpg");
                MilyUser milyUser5 = milyUserService.userSignup("test3333", "test3333", "박수희", "test3333@email.com", "01033333333", "1999-09-24").getData();
                milyUserService.lawyerSignup("성 범죄", "[성매매/성범죄] 초기 대응부터 확실하게", "법률사무소 M&Y", "3333-3333-3333", "경기", milyUser5, "https://i.postimg.cc/zGzFbxWg/1.jpg");
                MilyUser milyUser6 = milyUserService.userSignup("test4444", "test4444", "이재영", "test4444@email.com", "01044444444", "1997-12-07").getData();
                milyUserService.lawyerSignup("가족", "TV출연/대형 로펌 출신/이혼 관련 상담 2000건 이상", "MILY L&C", "4444-4444-4444", "경기", milyUser6, "https://i.postimg.cc/dtYHGRCT/Kakao-Talk-20231125-222200034.jpg");
                MilyUser milyUser7 = milyUserService.userSignup("test5555", "test5555", "정명준", "test5555@email.com", "01055555555", "1997-08-11").getData();
                milyUserService.lawyerSignup("교통사고/범죄", "교통 사고, 상해 전문 승소율 90% 이상 / 처리까지 최저 수임료로", "MILY L&C", "5555-5555-5555", "대전", milyUser7, "https://i.postimg.cc/Kc90KKbn/IMG-1638.jpg");
                MilyUser milyUser8 = milyUserService.userSignup("test6666", "test6666", "조승근", "test6666@email.com", "01066666666", "1999-03-04").getData();
                milyUserService.lawyerSignup("재산 범죄", "전세 사기 피해 관련 상담 313건의 노하우로 철저하게 도와드립니다.", "법률사무소 SPRING", "6666-6666-6666", "대구", milyUser8, "https://i.postimg.cc/FsDJtqbK/18449-26167-5550-1.jpg");

                milyUser3.setRole("approve");
                milyUser4.setRole("approve");
                milyUser5.setRole("approve");
                milyUser6.setRole("approve");
                milyUserRepository.save(milyUser3);
                milyUserRepository.save(milyUser4);
                milyUserRepository.save(milyUser5);
                milyUserRepository.save(milyUser6);

                MilyUser milyUser9 = milyUserService.userSignup("user0000", "user0000", "user0000", "user0000@email.com", "00000000000", "1991-11-13").getData();
                MilyUser milyUser10 = milyUserService.userSignup("user1111", "user1111", "user1111", "user1111@email.com", "01010101010", "1992-09-21").getData();
                MilyUser milyUser11 = milyUserService.userSignup("user2222", "user2222", "user2222", "user2222@email.com", "01020202020", "1989-05-30").getData();
                MilyUser milyUser12 = milyUserService.userSignup("user3333", "user3333", "user3333", "user3333@email.com", "01030303030", "1989-02-10").getData();
                MilyUser milyUser13 = milyUserService.userSignup("user4444", "user4444", "user4444", "user4444@email.com", "01040404040", "1988-04-12").getData();
                MilyUser milyUser14 = milyUserService.userSignup("user5555", "user5555", "user5555", "user5555@email.com", "01050505050", "1990-01-31").getData();
                MilyUser milyUser15 = milyUserService.userSignup("user6666", "user6666", "user6666", "user6666@email.com", "01060606060", "1998-12-06").getData();

                paymentService.dummyPayment("161251211", milyUser2, 300, "밀리 포인트 300", (long) 4800);
                paymentService.dummyPayment("161251212", milyUser3, 50, "밀리 포인트 50", (long) 900);
                paymentService.dummyPayment("161251213", milyUser3, 100, "밀리 포인트 100", (long) 1700);

                categoryService.addFC("성 범죄");
                categoryService.addFC("재산 범죄");
                categoryService.addFC("교통사고/범죄");
                categoryService.addFC("폭행/협박");
                categoryService.addFC("명예훼손/모욕");
                categoryService.addFC("기타 형사범죄");
                categoryService.addFC("부동산/임대차");
                categoryService.addFC("금전/계약 문제");
                categoryService.addFC("민사 절차");
                categoryService.addFC("기타 민사 문제");
                categoryService.addFC("가족");
                categoryService.addFC("회사");
                categoryService.addFC("의료/세금/행정");
                categoryService.addFC("IT/지식재산/금융");

                fc = categoryService.findByFTitle("성 범죄");

                categoryService.addSC("성매매", fc);
                categoryService.addSC("성폭력/강제추행 등", fc);
                categoryService.addSC("미성년 대상 성범죄", fc);
                categoryService.addSC("디지털 성범죄", fc);

                fc = categoryService.findByFTitle("재산 범죄");

                categoryService.addSC("횡령/배임", fc);
                categoryService.addSC("사기/공갈", fc);
                categoryService.addSC("기타 재산범죄", fc);

                fc = categoryService.findByFTitle("교통사고/범죄");

                categoryService.addSC("교통사고/도주", fc);
                categoryService.addSC("음주/무면허", fc);

                fc = categoryService.findByFTitle("폭행/협박");

                categoryService.addSC("폭행/협박/상해 일반", fc);

                fc = categoryService.findByFTitle("명예훼손/모욕");

                categoryService.addSC("명예훼손/모욕 일반", fc);
                categoryService.addSC("사이버 명예훼손/모욕", fc);

                fc = categoryService.findByFTitle("기타 형사범죄");

                categoryService.addSC("마약/도박", fc);
                categoryService.addSC("소년범죄/학교폭력", fc);
                categoryService.addSC("형사일반/기타범죄", fc);

                fc = categoryService.findByFTitle("부동산/임대차");

                categoryService.addSC("건축/부동산 일반", fc);
                categoryService.addSC("재개발/재건축", fc);
                categoryService.addSC("매매/소유권 등", fc);
                categoryService.addSC("임대차", fc);

                fc = categoryService.findByFTitle("금전/계약 문제");

                categoryService.addSC("손해배상", fc);
                categoryService.addSC("대여금/채권추심", fc);
                categoryService.addSC("계약일반/매매", fc);

                fc = categoryService.findByFTitle("민사 절차");

                categoryService.addSC("소송/집행절차", fc);
                categoryService.addSC("가압류/가처분", fc);
                categoryService.addSC("회생/파산", fc);

                fc = categoryService.findByFTitle("기타 민사 문제");

                categoryService.addSC("공증/내용증명/조합/국제문제 등", fc);

                fc = categoryService.findByFTitle("가족");

                categoryService.addSC("이혼", fc);
                categoryService.addSC("상속", fc);
                categoryService.addSC("가사 일반", fc);

                fc = categoryService.findByFTitle("회사");

                categoryService.addSC("기업법무", fc);
                categoryService.addSC("노동/인사", fc);

                fc = categoryService.findByFTitle("의료/세금/행정");

                categoryService.addSC("세금/행정/헌법", fc);
                categoryService.addSC("의료/식품의약", fc);
                categoryService.addSC("병역/군형법", fc);

                fc = categoryService.findByFTitle("IT/지식재산/금융");

                categoryService.addSC("소비자/공정거래", fc);
                categoryService.addSC("IT/개인정보", fc);
                categoryService.addSC("지식재산권/엔터", fc);
                categoryService.addSC("금융/보험", fc);

                FirstCategory firstCategory = categoryService.findByFTitle("성 범죄");
                SecondCategory secondCategory = categoryService.findBySTitle("성폭력/강제추행 등");
                milyXService.dummyCreate(milyUser2, firstCategory, secondCategory, "대학교 후배와 성관계 이후 상대방이 고소를 진행할 예정이랍니다.", "올해 초에 처음 관계를 맺었으며, 관계 이후에도 연락을 주고 받으면서 지냈습니다. 친하게 지냈음에도 불구하고, 5월에 상대방이 교수님과 상담하며 본인과 관계한 것에 대해 자궁에 상처가 났고, 성폭력을 당한 것처럼 주장했습니다. 처음 연락을 주고받은 시점부터 연락한 내용은 남아있습니다. 좋게 지내다가 돌연 사이가 소원해지면서 이전의 성관계에 대해 증거가 없다 생각해서 저러는 것 같은데, 어떻게 대처해야할까요? 무죄를 입증할만한 증거가 있다면 무죄 판정 받은 후에 무고죄도 성립될까요?", 50);
                milyUserService.sendEstimate(firstCategory, secondCategory, "대전", "내용", milyUser2);

                firstCategory = categoryService.findByFTitle("폭행/협박");
                secondCategory = categoryService.findBySTitle("폭행/협박/상해 일반");
                milyXService.dummyCreate(milyUser3, firstCategory, secondCategory, "술집에서 싸움에 휘말려 양방 폭행이 나왔습니다.", "사건 일시 : 2023년 10월 24일\n사건 경위 : 모르는 남성이 옆 테이블에 앉은 여성 일행에게 번호를 물었고, 일행이 완강하게 거부 의사를 밝혔으나 돌아가지 않고 계속 머뭇거려서 '자리로 돌아가세요 싫다잖아요'라고 한 마디 하자마자 얼굴을 한 대 맞았습니다.\n이후 저는 하지 말라는 의사를 두 차례 전달하였음에도 불구하고 밀치는 등의 행위를 계속 취해 와서 바닥에 넘어뜨렸는데 쌍방이랍니다.", 50);
                milyUserService.sendEstimate(firstCategory, secondCategory, "대전", "내용", milyUser3);

                firstCategory = categoryService.findByFTitle("명예훼손/모욕");
                secondCategory = categoryService.findBySTitle("사이버 명예훼손/모욕");
                milyXService.dummyCreate(milyUser12, firstCategory, secondCategory, "게임하다가 심한 욕설을 들었습니다.", "사건 발생 : 2023.09.10 11:35 a.m.\n" +
                        "본인 챔피언 : 카이사\n가해 챔피언 : 리 신\n내용 : 리그오브레전드 일반 (팀)게임을 하면서" +
                        "'만년브론즈희생폿(본인)아, 니 엄마는 너를 낳지 말았어야 한다', '이게 피임의 중요성이다 시', '발아 ㅋㅋ'" +
                        "라고 하며 저를 능욕했고, 수치심과 모욕감에 한참을 진정하지 못 했습니다.\n본인 챔피언인 카이사를 특정하며 말을 해서" +
                        "특정성과 팀 게임 특성상 공연성은 성립될 거 같은데, 모욕성까지 해서 모욕죄 성립 요건 3가지를 충족했으니 고소할 수 있을까요?", 50);
                milyUserService.sendEstimate(firstCategory, secondCategory, "대전", "내용", milyUser12);

                firstCategory = categoryService.findByFTitle("가족");
                secondCategory = categoryService.findBySTitle("이혼");
                milyXService.dummyCreate(milyUser10, firstCategory, secondCategory, "양육비와 재산분할에 대한 상담", "협의 이혼 준비 중이며\n" +
                        "양육비 산정표에 있는 금액이 한명의 아이에 대한 금액인가요?! 아니면 아이가 두명이면 2를 곱하면 되나요?!\n" +
                        "\n" +
                        "하기 조건인 경우에 월 양육비를 대략 어느정도 받을 수 있는지 계산 부탁드립니다.\n" +
                        "1. 2명의 자녀 19년 22년생(만1세, 만 4세) \n" +
                        "2. 남편 세후 통장에 찍히는 금액 약 450~460만원 \n" +
                        "와이프 21년도 연봉 5300만원 22년도 육아휴직 중간에 들어가서 2400만원 23년도 육아휴직 후 5월 퇴사하여 0원\n" +
                        "3. 대도시 거주 \n" +
                        "\n" +
                        "이 조건이면 약 월 양육비가 어느정도 되나요?", 70);

                firstCategory = categoryService.findByFTitle("교통사고/범죄");
                secondCategory = categoryService.findBySTitle("음주/무면허");
                MilyX milyX1 = milyXService.dummyCreate(milyUser14, firstCategory, secondCategory, "비접촉 사고의 보상 방법은?", "아침 출근길 정체구간 무리한 차량 끼어들기가 있어 피하기위해 피하려했으나 해당차량이 저를 차선 밖으로 밀어내 차량에 스크래치가 생겼습니다 블랙박스 영상 가지고있으며 해당 운전자를 신고해 보상을 받을수있는 방법이 있을까요?\n" +
                        "\n" +
                        "상대 차량 카니발 리무진 이며\n" +
                        "제 차량은 아우디 차량 입니다\n" +
                        "우측면 차량 도장 부분에 손상부분이 보이며\n" +
                        "해당 부분을 수리해야할것 같은데 해당 차량은 그냥 도망간 상황 입니다", 100);

                milyXCommentService.dummyCreate(milyX1, "상담자분께서 상대차량을 피하려고 핸들을 돌리지 않았다면 상대차량과 사고가 날 수 밖에 없었던 상황으로 판단된다면 비접촉사고에 해당될 수 있을 것으로 판단됩니다. 이와 같은 관점에서 사고에 대한 판단을 해보시면 좋을 것 같고, 이후 비접촉사고로 판단될 경우에는 상대방에게 손해의 배상을 청구하실 수 있을 것으로 보입니다.\n" +
                        "관련하여 궁금하신 사항이 있으시거나 조력이 필요하실 경우 편하게 연락주십시오. 감사합니다.", milyUser4);

                firstCategory = categoryService.findByFTitle("명예훼손/모욕");
                secondCategory = categoryService.findBySTitle("사이버 명예훼손/모욕");
                milyXService.dummyCreate(milyUser11, firstCategory, secondCategory, "고소접수 후 수사관 연락 시기와 절차에 대한 질문", "안녕하세요 저는 피고소인 입장입니다. 디시인사이드 마이너 갤러리에서 성적 발언을 해서 통매음으로 고소당한 사건입니다. 저번 주에 갤러리를 통해 고소접수를 했다는 글을 올려서 알게되었습니다. 고소인은 pdf, 아카이브를 다 따놔있는 상태라서 현재 저는 문제되는 해당 계정을 삭제하고 탈퇴를 진행중입니다. (원본 삭제) 고소인은 고소 다음주에 바로 진술하러간다고 하는데 보통 이렇게 빨리 진술하러 가는 걸까요? 피고소인인 저에게 수사관이 연락오려면 어느 정도 시간이 걸릴까요?\n" +
                        "\n" +
                        "그리고 이관될 때 수사관이 전화로\n" +
                        "제 신상을 물어본 다음 우편으로 이관됐다고 나오는 게 맞는 걸까요?", 50);

                firstCategory = categoryService.findByFTitle("교통사고/범죄");
                secondCategory = categoryService.findBySTitle("교통사고/도주");
                Feed feed1 = feedService.dummyCreate(milyUser4, firstCategory, secondCategory, "뺑소니 사고에 관한 법적 조치", "연말 행사로 인한 잦은 회식과 망년회 모임등으로 인해서 음주를 하고 운전대를 잡는 경우가 있습니다.\n" + "그럴경우 대리운전을 불러 운전을 맡기거나, 혹은 대중교통을 이용하여 귀가하시는 것이 옳은 방법인데요 허나 몇몇 분들이 음주를 하신상태에서 운전을 하시다가 사고를 내시고는 음주한 사실이 들키기 싫어서 도주하는 경향이 있습니다. 술을 먹으면 인간의 호르몬 영향으로 인해서 판단력이 흐려지고 수습하기보다는 도망하는 쪽으로 본능이 나오게 되는데 그래서 뺑소니 사고를 내시고는 갑니다. \n" +
                        "그럴경우엔 법적으로 상황이 많이 불리해질 수 있습니다. 모임 회사 망년회에 참여할 경우에는 운전을 절대적으로 삼가하시고 혹여 차가 필요할 경우엔 가져오시되 대리운전을 꼭 불러서 하시고, 가급적으로 택시를 이용해서 다니시기를 권장합니다. \n" +
                        "4~5만원 아낄려다가 수 천만원이 깨지고 심하면 징역살이를 할 수도 있습니다. 꼭 안전운전 하시고 절대적으로 음주 후 운전대를 잡지 마시길 바랍니다.");

                firstCategory = categoryService.findByFTitle("가족");
                secondCategory = categoryService.findBySTitle("이혼");
                Feed feed2 = feedService.dummyCreate(milyUser7, firstCategory, secondCategory, "대한민국 이혼율 0.4% 감소", "2022년 혼인건수는 19만 2,000건으로 2021년보다 800건, 0.4% 감소했습니다.\n" +
                        "\n" +
                        "인구 1,000명당 혼인건수를 의미하는 조혼인율은 3.7건으로 2021년보다 0.1건 감소했습니다.\n" +
                        "\n" +
                        "2022년 혼인건수와 조혼인율 모두 1970년 통계 작성 이후 가장 낮았습니다.\n" +
                        "\n" +
                        "연령대별 혼인건수는 남녀 모두 20대 후반 연령대에서 가장 많이 감소했습니다.\n" +
                        "\n" +
                        "해당 연령인구 1,000명당 혼인건수를 의미하는 연령별 혼인율을 보면 남자는 30대 초반에서 40.3건, 여자는 30대 초반에서 41.3건으로 가장 높게 나타났습니다.\n" +
                        "\n" +
                        "평균 초혼연령은 남자 33.7세, 여자 31.3세로 2021년보다 각각 0.4세, 0.2세 높아졌습니다.\n" +
                        "\n" +
                        "2022년 이혼건수는 9만 3,000건으로 2021년보다 8,000건, 8.3% 감소했습니다.\n" +
                        "\n" +
                        "인구 1,000명당 이혼건수인 조이혼율은 1.8건으로 2021년보다 0.2건 감소했습니다.\n" +
                        "\n" +
                        "혼인지속기간별로 이혼의 구성비를 살펴보면 4년 이하 18.6%, 5~9년 18.0%, 30년 이상 16.8% 순으로 많았습니다.\n" +
                        "\n" +
                        "연령별 이혼율은 남자는 40대 초반이 1,000명당 6.9건, 여자는 40대 초반이 1,000명 7.6건으로 가장 높았습니다.\n" +
                        "\n" +
                        "외국인과의 혼인은 1만 7,000건으로 2021년보다 4,000건, 27.2% 증가했습니다.\n" +
                        "\n" +
                        "외국인과의 이혼은 6,000건으로 2021년보다 400건, 5.9% 감소했습니다.\n" +
                        "\n" +
                        "[출처] 대한민국 정책브리핑(www.korea.kr)");

                firstCategory = categoryService.findByFTitle("재산 범죄");
                secondCategory = categoryService.findBySTitle("횡령/배임");
                Feed feed3 = feedService.dummyCreate(milyUser9, firstCategory, secondCategory, "지인에게 코인투자를 부탁받았는데 고소를 당한 사례", "안녕하세요 재산범죄 담당자 조승근입니다. 최근 횡령/배임에 대한 사건 사례를 한 가지 소개드릴려고 합니다" +
                        "횡령배임으로 고소가 되었습니다. 저에게 코인 투자를 맡겼던 사람이 제가 투자 손실을 보자 횡령배임으로 고소를 했습니다. 횡령배임은 어떤 경우에 성립될 수 있나요? 아직 고소장을 확인하지 못했는데 고소장 확인은 어떻게 하나요? 무혐의를 받고 싶은데 경찰조사에서 어떻게 해야할까요?" +
                        "라는 질문입니다." +
                        "답변을 드리자면 횡령, 배임 관련 형사사건의 구체적인 고소 내용을 확인하여 이에 대응하는 적절한 진행 및 대응 방안을 마련하셔야 합니다. 담당 관할 경찰서에 정보 공개 청구 등을 통해서 해당 횡령, 배임 관련 형사사건의 고소 내용을 확인할 수 있습니다. ");
            }
        };
    }
}