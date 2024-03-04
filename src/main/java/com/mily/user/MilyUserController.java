package com.mily.user;

import com.mily.article.milyx.MilyX;
import com.mily.article.milyx.MilyXService;
import com.mily.article.milyx.category.CategoryService;
import com.mily.article.milyx.category.entity.FirstCategory;
import com.mily.article.milyx.category.entity.SecondCategory;
import com.mily.article.milyx.comment.MilyXComment;
import com.mily.article.milyx.comment.MilyXCommentService;
import com.mily.base.rq.Rq;
import com.mily.base.rsData.RsData;
import com.mily.estimate.Estimate;
import com.mily.estimate.EstimateRepository;
import com.mily.payment.PaymentService;
import com.mily.reservation.Reservation;
import com.mily.reservation.ReservationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.*;

@RequestMapping("/user")
@RequiredArgsConstructor
@Controller
public class MilyUserController {
    private final Rq rq;
    private final MilyUserService milyUserService;
    private final CategoryService categoryService;
    private final MilyXService milyXService;
    private final MilyXCommentService milyXCommentService;
    private final PaymentService paymentService;
    private final ReservationService reservationService;
    private final EstimateRepository estimateRepository;
    private final HttpServletRequest httpServletRequest;

    @PreAuthorize("isAnonymous()")
    @GetMapping("/login")
    public String showUserLogin() {
        String referer = httpServletRequest.getHeader("Referer");
        httpServletRequest.getSession().setAttribute("previousUrl", referer);

        return "mily/milyuser/login_form";
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String showSignup() {
        if (milyUserService.getCurrentUser() == null) {
            return "mily/milyuser/signup_form";
        } else {
            return "redirect:/";
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/signup")
    public String doSignup(@Valid SignupForm signupForm) {
        RsData<MilyUser> signupRs = milyUserService.userSignup(
                signupForm.getUserLoginId(),
                signupForm.getUserPassword(),
                signupForm.getUserName(),
                signupForm.getUserEmail(),
                signupForm.getUserPhoneNumber(),
                signupForm.getUserDateOfBirth()
        );

        if (signupRs.isFail()) {
            rq.historyBack(signupRs.getMsg());
            return "common/js";
        }

        return rq.redirect("/", signupRs.getMsg());
    }

    @PreAuthorize("isAnonymous()")
    @GetMapping("/lawyerSignup")
    public String showLawyerSignup(Model model) {
        if (milyUserService.getCurrentUser() == null) {
            List<FirstCategory> categories = categoryService.getFirstCategories();
            model.addAttribute("categories", categories);
            return "mily/milyuser/lawyer_signup_form";
        } else {
            return "redirect:/";
        }
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/lawyerSignup")
    public String doLawyerSignup(@Valid LawyerSignupForm lawyerSignupForm, @Valid SignupForm signupForm) {
        RsData<MilyUser> signupRs1 = milyUserService.userSignup(
                signupForm.getUserLoginId(),
                signupForm.getUserPassword(),
                signupForm.getUserName(),
                signupForm.getUserEmail(),
                signupForm.getUserPhoneNumber(),
                signupForm.getUserDateOfBirth()
        );

        MilyUser milyUser = signupRs1.getData();

        RsData<LawyerUser> signupRs2 = milyUserService.lawyerSignup(
                lawyerSignupForm.getMajor(),
                lawyerSignupForm.getIntroduce(),
                lawyerSignupForm.getOfficeAddress(),
                lawyerSignupForm.getLicenseNumber(),
                lawyerSignupForm.getArea(),
                milyUser,
                lawyerSignupForm.getProfileImg()
        );

        if (signupRs2.isFail()) {
            rq.historyBack(signupRs2.getMsg());
            return "common/js";
        }

        return rq.redirect("/", signupRs2.getMsg());
    }

    @Getter
    @AllArgsConstructor
    public static class SignupForm {
        @NotBlank
        private String userLoginId;

        @NotBlank
        private String userPassword;

        @NotBlank
        private String userName;

        @NotBlank
        @Email
        private String userEmail;

        @NotBlank
        private String userPhoneNumber;

        @NotBlank
        private String userDateOfBirth;
    }

    @Getter
    @AllArgsConstructor
    @ToString
    public static class LawyerSignupForm {
        public MilyUser milyUser;

        @NotBlank
        private String major;

        @NotBlank
        private String introduce;

        @NotBlank
        private String officeAddress;

        @NotBlank
        private String licenseNumber;

        @NotBlank
        private String area;

        private MultipartFile profileImg;
    }

    @GetMapping("checkUserLoginIdDup")
    @ResponseBody
    public RsData checkUserLoginIdDup(String userLoginId) {
        return milyUserService.checkUserLoginIdDup(userLoginId);
    }

    @GetMapping({"checkUserEmailDup", "/mypage/edit/checkUserEmailDup"})
    @ResponseBody
    public RsData checkUserEmail(String userEmail) {
        return milyUserService.checkUserEmailDup(userEmail);
    }

    @GetMapping({"checkUserPhoneNumberDup", "/mypage/edit/checkUserPhoneNumberDup"})
    @ResponseBody
    public RsData checkUserPhoneNumber(String userPhoneNumber) {
        return milyUserService.checkUserPhoneNumberDup(userPhoneNumber);
    }

    @GetMapping("/estimate")
    public String showForm(EstimateCreateForm estimateCreateForm, Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (isLoginedUser != null && isLoginedUser.role.equals("member")) {
            List<FirstCategory> firstCategories = categoryService.getFirstCategories();
            List<SecondCategory> secondCategories = categoryService.getSecondCategories();

            model.addAttribute("firstCategories", firstCategories);
            model.addAttribute("secondCategories", secondCategories);

            return "estimate";
        } else {
            return "redirect:/";
        }
    }

    @PostMapping("/estimate")
    public String getEstimate(@Valid EstimateCreateForm estimateCreateForm) {
        MilyUser milyUser = milyUserService.getCurrentUser();

        if (!milyUser.getRole().equals("member")) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }

        milyUserService.sendEstimate(estimateCreateForm.getFirstCategory(), estimateCreateForm.getSecondCategory(), estimateCreateForm.getArea(), estimateCreateForm.getBody(), milyUser);
        return rq.redirect("/", "견적서가 전달되었습니다.");
    }

    @Getter
    @AllArgsConstructor
    public class EstimateCreateForm {
        private FirstCategory firstCategory;

        private SecondCategory secondCategory;

        @NotBlank
        private String area;

        @NotBlank
        private String body;
    }

    @GetMapping("/waitLawyerList")
    public String getWaitingLawyerList(Principal principal, Model model) {
        String userLoginId = principal.getName();
        if (milyUserService.isAdmin(userLoginId)) {
            List<MilyUser> waitingLawyers = milyUserService.getWaitingLawyerList();
            model.addAttribute("waitingLawyers", waitingLawyers);
            return "/mily/waiting_lawyer_list";
        } else {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }
    }

    @PostMapping("/approveLawyer/{id}")
    public String approveLawyer(@PathVariable int id, Principal principal) {
        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        String adminLoginId = principal.getName();
        milyUserService.approveLawyer(id, adminLoginId);

        return "redirect:" + referer;
    }

    // 아이디 찾기 페이지를 보여주는 핸들러
    @PreAuthorize("isAnonymous()")
    @GetMapping("/findId")
    public String showFindId() {
        return "mily/milyuser/find_id_form";  // 해당 페이지의 경로와 이름을 알맞게 수정하세요.
    }

    // 비밀번호 찾기 페이지를 보여주는 핸들러
    @PreAuthorize("isAnonymous()")
    @GetMapping("/findPassword")
    public String showFindPassword() {
        return "mily/milyuser/find_password_form";
    }

    @PreAuthorize("isAnonymous()")
    @PostMapping("/findPassword")
    public String findPassword(@RequestParam String userLoginId, @RequestParam String userEmail, RedirectAttributes redirectAttributes) {
        // findByUsernameAndEmail을 호출하여 사용자를 찾습니다.
        return milyUserService.findByuserLoginIdAndEmail(userLoginId, userEmail)
                .map(member -> {
                    // 임시 비밀번호 발송 로직을 실행합니다.

                    milyUserService.sendTempPasswordToEmail(member);
                    // 성공 메시지와 함께 로그인 페이지로 리다이렉트합니다.
                    redirectAttributes.addFlashAttribute("message", "해당 회원의 이메일로 임시 비밀번호를 발송하였습니다.");
                    return "redirect:/user/login?lastUsername=" + member.getUserLoginId();
                })
                .orElseGet(() -> {
                    // 사용자를 찾을 수 없을 경우 에러 메시지를 설정하고 이전 페이지로 이동합니다.
                    redirectAttributes.addFlashAttribute("errorMessage", "일치하는 회원이 존재하지 않습니다.");
                    return "redirect:/mily/milyuser/find_password_form";
                });
    }

    @PostMapping("/retrieveId")
    public String retrieveId(@RequestParam String userEmail, Model model, RedirectAttributes redirectAttributes) {
        MilyUser milyUser = milyUserService.findUserLoginIdByEmail(userEmail);
        model.addAttribute("foundId", milyUser.getUserLoginId());
        return "mily/milyuser/retrieve_id_result";
    }

    @PostMapping("/retrievePassword")
    public String retrievePassword(@RequestParam String userEmail, String userloginId, Model model, RedirectAttributes redirectAttributes) {
        MilyUser milyUser = milyUserService.findUserLoginIdByEmail(userEmail);
        model.addAttribute("foundPassword", milyUser.getUserPassword());
        return "mily/milyuser/retrieve_password_result";
    }

    @PostMapping("/findLoginIdPage")
    public ResponseEntity<String> retrieveId(@RequestParam("userEmail") String userEmail) {
        MilyUser milyUser = milyUserService.findUserLoginIdByEmail(userEmail);
        if (milyUser.getUserEmail().equals(userEmail)) {
            return ResponseEntity.ok("");
        } else {
            return ResponseEntity.badRequest().body("아이디를 찾을 수 없습니다.");
        }
    }

    @GetMapping("getEstimate")
    public String getEstimate(Model model) {
        MilyUser user = milyUserService.getCurrentUser();

        if (!user.getRole().equals("approve")) {
            return rq.redirect("/", "접근 권한이 없습니다.");
        }

        String category = milyUserService.getCurrentUser().getLawyerUser().getMajor();
        FirstCategory firstCategory = categoryService.findByFTitle(category);
        String area = milyUserService.getCurrentUser().getLawyerUser().getArea();
        List<Estimate> estimates = milyUserService.getEstimate(LocalDateTime.now(), firstCategory, area);
        model.addAttribute("estimates", estimates);
        return "estimate_list";
    }

    /* 마이 페이지, 관리자 대시 보드, 변호사 대시 보드 */
    @GetMapping("/mypage/info")
    public String showMyPage(Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        // 현재 로그인 상태가 아닌 유저의 요청을 받으면 돌려 보냄.
        if (isLoginedUser == null) {
            return "redirect:/";
        }

        // 사용자의 전화 번호를 가리는 작업
        String phoneNumber = isLoginedUser.getUserPhoneNumber();
        phoneNumber = phoneNumber.substring(0, 3) + "-***" + phoneNumber.substring(6, 7) + "-**" + phoneNumber.substring(9);

        // 사용자의 이메일을 가리는 작업
        String email = milyUserService.maskEmail(isLoginedUser.getUserEmail());

        model.addAttribute("userPhone", phoneNumber);
        model.addAttribute("userEmail", email);

        // 현재 로그인 된 사용자의 권한이 "member"일 때
        if (isLoginedUser.getRole().equals("member")) {
            model.addAttribute("user", isLoginedUser);

            // 사용자가 작성 한 글
            List<MilyX> userPosts = milyXService.findByAuthor(isLoginedUser);

            model.addAttribute("posts", userPosts.size());
            model.addAttribute("userPosts", userPosts);

            // 사용자의 상담 예약 내역
            List<Reservation> userReservation = reservationService.findByMilyUser(isLoginedUser);
            model.addAttribute("reservations", userReservation.size());
            model.addAttribute("reservation", userReservation);

            // 사용자의 포인트 충전 내역
            if (isLoginedUser.getPayments() != null) {
                model.addAttribute("payments", isLoginedUser.getPayments().size());
            }

            /* 내 정보 페이지가 기본값으로 적용 됨 */
            return "/mily/milyuser/information/member/info";
        }

        // 현재 로그인 된 사용자의 권한이 "lawyer"일 때
        if (isLoginedUser.getRole().equals("approve")) {
            // 사용자가 작성 한 답변
            List<MilyXComment> userComments = milyXCommentService.findAuthorId(isLoginedUser.getId());

            model.addAttribute("user", isLoginedUser);
            model.addAttribute("commentsCount", userComments.size());
            model.addAttribute("comments", userComments);

            // 상담 예약
            List<Reservation> reservations = reservationService.findByLawyerUserId(isLoginedUser.getId());
            model.addAttribute("reservationCount", reservations.size());
            model.addAttribute("reservation", reservations);

            // 모든 견적서 보이게
            List<Estimate> estimateList = estimateRepository.findAll();

            model.addAttribute("estimatesCount", estimateList.size());
            model.addAttribute("estimate", estimateList);

            return "/mily/milyuser/information/lawyer/info";
        }

        // 현재 로그인 된 사용자의 권한이 "admin"일 때
        if (isLoginedUser.getRole().equals("admin")) {
            showDashboard(model);
            return "/mily/milyuser/information/admin/admin_dashboard";
        }

        return "redirect:" + referer;
    }

    @GetMapping("/mypage/dashboard")
    public String showDashboard(Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (isLoginedUser != null) {
            if (isLoginedUser.getRole().equals("approve")) {
                List<MilyXComment> allComments = milyXCommentService.findAuthorId(isLoginedUser.getId());
                model.addAttribute("commentsCount", allComments.size());
                model.addAttribute("comments", allComments);

                List<Reservation> allReservations = reservationService.findByLawyerUserId(isLoginedUser.getId());
                List<Reservation> sortedReservations = allReservations.stream()
                        .sorted(Comparator.comparing(Reservation::getReservationTime))
                        .toList();
                model.addAttribute("reservationsCount", allReservations.size());
                model.addAttribute("reservations", sortedReservations);

                List<Estimate> allEstimates = estimateRepository.findAll();
                model.addAttribute("allEstimatesCount", allEstimates.size());
                model.addAttribute("allEstimates", allEstimates);

                // 달력 메서드
                List<LocalDateTime> dates = new ArrayList<>();
                List<String> daysOfWeek = new ArrayList<>();

                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = start.plusDays(7);

                model.addAttribute("start", start);
                model.addAttribute("end", end);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd");

                dates.add(start);
                daysOfWeek.add("오늘");

                for (int i = 1; i < 7; i++) {
                    dates.add(start.plusDays(i));
                    String dayOfWeek = start.plusDays(i).getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.KOREAN);
                    daysOfWeek.add(dayOfWeek.substring(0, 1));
                }

                model.addAttribute("dates", dates);
                model.addAttribute("day", daysOfWeek);

                return "mily/milyuser/information/lawyer/lawyer_dashboard";
            } else if (isLoginedUser.getRole().equals("admin")) {
                List<MilyUser> allUsers = milyUserService.findAll();
                List<MilyUser> allLawyers = milyUserService.findByRole("approve");

                model.addAttribute("usersCount", allUsers.size());
                model.addAttribute("lawyersCount", allLawyers.size());

                List<MilyX> allPosts = milyXService.findAll();
                List<Estimate> allEstimates = estimateRepository.findAll();
                List<Reservation> allReservations = reservationService.findAll();

                model.addAttribute("posts", allPosts.size());
                model.addAttribute("estimates", allEstimates.size());
                model.addAttribute("reservations", allReservations.size());

                List<MilyUser> waitingLawyers = milyUserService.findByRole("waiting");

                model.addAttribute("waiting", waitingLawyers);
                model.addAttribute("waitingCount", waitingLawyers.size());

                return "mily/milyuser/information/admin/admin_dashboard";
            } else {
                return "redirect:/";
            }
        }

        return "redirect:/";
    }

    /* 내 정보 수정 */
    @GetMapping("/mypage/edit")
    public String getEditInformation(Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        if (isLoginedUser != null) {
            model.addAttribute("user", isLoginedUser);
            return "mily/milyuser/information/member/edit";
        }

        return "redirect:" + referer;
    }

    @PostMapping("/mypage/edit/other")
    public String doEditInformation(@RequestParam String userEmail, @RequestParam String userPhoneNumber, Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (userEmail.isEmpty()) {
            userEmail = isLoginedUser.getUserEmail();
        }

        if (userPhoneNumber.isEmpty()) {
            userPhoneNumber = isLoginedUser.getUserPhoneNumber();
        }

        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        if (isLoginedUser != null) {
            milyUserService.editInformation(isLoginedUser, userEmail, userPhoneNumber);
            model.addAttribute("user", isLoginedUser);

            return "redirect:" + referer;
        }

        return "redirect:" + referer;
    }

    @GetMapping("/mypage/edit/other")
    public String getEdiitPassword(Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        if (isLoginedUser != null) {
            model.addAttribute("user", isLoginedUser);
            return "mily/milyuser/information/member/other";
        }

        return "redirect:" + referer;
    }

    @PostMapping("/mypage/edit/password")
    public String postEditPassword(@RequestParam String userPasswordConfirm, @RequestParam String userPassword, @RequestParam String userPassword2) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (milyUserService.checkPassword(isLoginedUser, userPasswordConfirm)) {
            if (userPassword.equals(userPassword2)) {
                milyUserService.editPassword(isLoginedUser, userPassword);
                return "redirect:/user/mypage/info";
            }
        } else {
            return "redirect:/user/mypage/info";
        }
        return "redirect:/user/mypage/info";
    }

    /* 비밀번호 체크 */
    @PostMapping("/checkpassword")
    public ResponseEntity<Boolean> checkPassword(@RequestBody Map<String, String> payload) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();
        String rawPassword = payload.get("password");

        return ResponseEntity.ok(milyUserService.checkPassword(isLoginedUser, rawPassword));
    }

    @GetMapping("/mypage/withdraw")
    public String showWithdraw(Model model) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (isLoginedUser != null) {
            model.addAttribute("user", isLoginedUser);
            return "mily/milyuser/information/member/withdraw";
        }

        return "mily/milyuser/information/withdraw_success";
    }

    @GetMapping("/mypage/withdraw/ok")
    public String doWithdraw() {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        // 경로 이동 요청 전, 머물던 URL 을 받아 온다.
        String referer = httpServletRequest.getHeader("Referer");

        if (isLoginedUser != null) {
            milyUserService.withdraw(isLoginedUser);
            return "redirect:/user/logout";
        }

        return "redirect:" + referer;
    }

    @GetMapping("lawyers")
    public String lawyerLists(Model model) {
        List<MilyUser> lawyers = milyUserService.findByRole("approve");
        model.addAttribute("lawyers", lawyers);
        return "lawyers";
    }
}