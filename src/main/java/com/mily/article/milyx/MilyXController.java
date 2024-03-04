package com.mily.article.milyx;

import com.mily.article.milyx.category.CategoryService;
import com.mily.article.milyx.category.entity.FirstCategory;
import com.mily.article.milyx.category.entity.SecondCategory;
import com.mily.article.milyx.comment.MilyXComment;
import com.mily.article.milyx.comment.MilyXCommentService;
import com.mily.base.rsData.RsData;
import com.mily.standard.util.Ut;
import com.mily.user.MilyUser;
import com.mily.user.MilyUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Controller
@RequiredArgsConstructor
@RequestMapping("/milyx")
public class MilyXController {
    private final CategoryService categoryService;
    private final MilyXService milyXService;
    private final MilyXCommentService milyXCommentService;
    private final MilyUserService milyUserService;
    private final HttpServletRequest httpServletRequest;

    @PreAuthorize("isAnonymous()")
    @GetMapping("")
    public String showMilyX(Model model) {
        // 모든 상담글을 받아와서 오래된 날짜 순으로 정렬합니다.
        List<MilyX> milyx = new java.util.ArrayList<>(milyXService.getAllPosts().stream()
                .sorted(Comparator.comparing(MilyX::getCreateDate))
                .toList());

        Collections.reverse(milyx); // 최신순으로 재정렬합니다.
        model.addAttribute("milyx", milyx);

        List<MilyXComment> comments = milyXCommentService.findAll(); // 모든 댓글을 받아옵니다.
        Collections.reverse(comments); // 최신순으로 정렳합니다.

        model.addAttribute("count", comments.size()); // View 에서 댓글 수 리소스를 제공할 수 있게 해줍니다.
        model.addAttribute("comments", comments);

        try {
            MilyUser isLoginedUser = milyUserService.getCurrentUser();
            if (isLoginedUser != null) {
                model.addAttribute("user", isLoginedUser);
            }
            return "mily/milyx/milyx_index";
        } catch (NullPointerException e) {
            return "mily/milyx/milyx_index";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String create(Model model) {
        String referer = httpServletRequest.getHeader("Referer");
        /* HttpServletRequest 에서 유저가 페이지 이동을 요청할 때, 이전에 머물던 페이지의
           경로를 받아 와서 저장합니다. 이는 페이지 이동 요청 시 권한이 없을 때 사용됩니다. */

        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        // 로그인 상태가 아니라면 이전에 머물던 페이지로 보냅니다.
        if (isLoginedUser == null) {
            return "redirect:/" + referer;
        }

        // 현재 로그인한 유저의 권한이 "member" (일반 회원)가 아니라면 이전에 머물던 페이지로 보냅니다.
        if (!isLoginedUser.getRole().equals("member")) {
            return "redirect:/" + referer;
        }

        int userPoint = isLoginedUser.getMilyPoint(); // 현재 로그인한 유저의 milyPoint (사이버 머니)를 받아옵니다.
        model.addAttribute("myPoint", userPoint);

        /* MilyX (상담글)는 50~100 포인트를 사용해야만 작성이 가능합니다.
           즉, 현재 로그인한 유저의 milyPoint 데이터가 들어있는 userPoint가
           50 미만이라면 milyPoint (사이버 머니) 충전 페이지로 보냅니다. */
        if (userPoint < 50) {
            return "redirect:/payment";
        }

        int maxPoint = Math.min(userPoint, 100); // 해당 유저가 가진 포인트 혹은 100 중 작은 값으로 최대값을 결정합니다.

        // 50부터 100까지 5 단위의 숫자 리스트를 생성합니다.
        List<Integer> pointOptions = IntStream.rangeClosed(50, maxPoint)
                .filter(i -> i % 5 == 0)
                .boxed()
                .collect(Collectors.toList());
        model.addAttribute("pointOptions", pointOptions);

        // 카
        List<FirstCategory> firstCategories = categoryService.getFirstCategories();
        model.addAttribute("firstCategories", firstCategories);
        return "mily/milyx/milyx_create";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String create(@RequestParam int firstCategory, @RequestParam String secondCategory, @RequestParam String subject, @RequestParam String body, @RequestParam int point) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        FirstCategory fc = categoryService.findByFId(firstCategory);
        SecondCategory sc = categoryService.findBySId(Integer.parseInt(secondCategory));

        RsData<MilyX> rsData = milyXService.create(isLoginedUser, fc, sc, subject, body, point);
        return "redirect:/milyx";
    }

    @GetMapping("/secondCategories") // Ajax 비동기 통신을 통해 SecondCategory 리소스를 페이지 새로고침 없이 제공합니다.
    public ResponseEntity<List<SecondCategory>> validateId(@RequestParam(value = "firstCategoryId") int firstCategoryId) {
        List<SecondCategory> sc = categoryService.findByFirstCategoryId(firstCategoryId); // 유저가 요청한 1차 카테고리에 해당하는 2차 카테고리 리스트를 생성합니다.
        return ResponseEntity.ok().body(sc); // 생성한 2차 카테고리 리스트를 반환합니다. Ajax 비동기 통신을 통해 유저의 UX 가 향상되는 효과를 기대해 볼 수 있습니다.
    }

    @AllArgsConstructor
    @Getter
    public static class CreateForm {
        private FirstCategory firstCategory;
        private SecondCategory secondCategory;
        private String subject;
        private String body;
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/detail/{id}")
    public String showDetail(Model model, @PathVariable long id) {
        try {
            MilyUser isLoginedUser = milyUserService.getCurrentUser();
            if (isLoginedUser != null) {
                String confirmRole = isLoginedUser.getRole();
                model.addAttribute("role", confirmRole);
                model.addAttribute("user", isLoginedUser);
            }
            MilyX milyX = milyXService.findById(id).get(); // 사용자가 게시글의 상세 보기를 요청할 때 해당 글의 id 를 받아와서 저장합니다.
            int view = milyX.getView() + 1; // 해당 글의 조회수에 1을 더한 값을 view 에 저장합니다.

            // 새로 갱신된 조회수를 저장합니다.
            MilyX mx = MilyX.builder()
                    .view(view)
                    .build();

            milyX.updateView(view);
            milyXService.updateView(milyX.getId(), mx);

            model.addAttribute("milyx", milyX);
            model.addAttribute("isAuthor", milyX.getAuthor().getId() == (isLoginedUser.getId()));

            return "mily/milyx/milyx_detail";
        } catch (NullPointerException e) {
            return "mily/milyx/milyx_detail";
        }
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String getModify(Model model, @PathVariable long id) {
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        MilyX milyX = milyXService.findById(id).get(); // 수정하려는 게시글의 정보를 받아와서 저장합니다.

        // 수정하려는 게시글의 작성자가 맞는 지 확인합니다.
        if (milyX.getAuthor().getId() != isLoginedUser.getId()) {
            return "redirect:/milyx/detail/" + id;
        }

        // 수정하려는 게시물의 댓글 유무 확인
        if (!milyX.getComments().isEmpty()) {
            /* 수정하려는 게시글에 댓글이 존재한다면 수정이 불가능합니다.
               고로 수정하고자 했던 게시글의 detail 로 돌려 보냅니다. */
            return "redirect:/milyx/detail/" + id;
        }

        model.addAttribute("milyx", milyX);

        return "mily/milyx/milyx_modify";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String doModify(@PathVariable Long id, @RequestParam String subject, @RequestParam String body) {
        milyXService.modify(id, subject, body);
        return "redirect:/milyx/detail/" + id;
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/delete/{id}")
    public String doDelete(@PathVariable Long id) {
        MilyX mx = milyXService.findById(id).orElse(null);
        MilyUser isLoginedUser = milyUserService.getCurrentUser();

        if (mx == null) {
            return "redirect:/milyx/detail" + id;
        }

        MilyX milyX = milyXService.findById(id).get();

        // 현재 로그인한 유저가 관리자인 지 확인합니다.
        if (!isLoginedUser.getUserLoginId().equals("admin999")) {
            // 삭제하려는 게시글의 작성자인 지 확인합니다.
            if (milyX.getAuthor().getId() != isLoginedUser.getId()) {
                /* 현재 로그인한 유저가 관리자, 작성자가 아니라는 조건에 부합한다면 해당 게시글로 다시 돌려보냅니다.
                   하지만 View 에서 thymeleaf template engine 을 통해 <th:if="${isAuthor}"> 조건에 부합 해야만
                   삭제 버튼이 보이기에 이중 잠금 개념으로 보시면 되겠습니다. */
                return "redirect:/milyx/detail" + id;
            }

            // 삭제하려는 게시글의 댓글 유무를 확인합니다.
            if (!milyX.getComments().isEmpty()) {
                return "redirect:/milyx/detail" + id;
            }
        }

        milyXService.delete(id);

        return "redirect:/milyx"; // 게시글의 삭제는 detail form 에서 요청해 오기 때문에 게시글 목록 페이지로 보냅니다.
    }
}