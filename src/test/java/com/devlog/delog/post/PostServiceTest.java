package com.devlog.delog.post;

import com.devlog.delog.controller.account.SignUpRequest;
import com.devlog.delog.controller.post.PostingRequest;
import com.devlog.delog.domain.Account;
import com.devlog.delog.domain.Post;
import com.devlog.delog.error.NotFoundException;
import com.devlog.delog.service.AccountService;
import com.devlog.delog.service.PostService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PostServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostService postService;

    @Autowired
    private AccountService accountService;

    private Long postId;

    private Account newAccount;

    @BeforeAll
    void setUp() {
        SignUpRequest signUpRequest = new SignUpRequest();
        signUpRequest.setPrincipal("test123@email.com");
        signUpRequest.setCredentials("1234");
        signUpRequest.setName("racoon");
        postId = 1L;
        newAccount = accountService.processNewAccount(signUpRequest);
    }

    @Test
    @Order(1)
    void 포스트를_작성한다() {
        String contents = randomAlphabetic(40);
        String title = randomAlphabetic(10);
        PostingRequest postingRequest = new PostingRequest();
        postingRequest.setTitle(title);
        postingRequest.setContents(contents);
        Post post = postService.write(postingRequest, newAccount);
        assertThat(post, is(notNullValue()));
        assertThat(post.getTitle(), is(title));
        assertThat(post.getContents(), is(contents));
        assertThat(post.getAccount(), is(newAccount));
        log.info("Written post: {}", post);
    }

    @Test
    @Order(2)
    void 포스트_목록을_조회한다() {
        List<Post> posts = postService.findAll(0, 1);
        assertThat(posts, is(notNullValue()));
        assertThat(posts.size(), is(1));
    }

    @Test
    @Order(3)
    void 포스트를_처음으로_좋아한다() {
        Post post = postService.findPost(postId).orElseThrow(() -> new NotFoundException(Post.class, postId));
        assertThat(post, is(notNullValue()));
        assertThat(post.isLikesOfMe(), is(false));
        Account account = post.getAccount();
        int beforeLikes = post.getLikes();
        assertThat(account, is(notNullValue()));

        post = postService.like(postId, account).orElse(null);
        assertThat(post, is(notNullValue()));
        assertThat(post.isLikesOfMe(), is(true));
        assertThat(post.getLikes(), is(beforeLikes + 1));
    }

    @Test
    @Order(4)
    void 포스트를_중복으로_좋아할수없다() {
        Post post = postService.findPost(postId).orElseThrow(() -> new NotFoundException(Post.class, postId));
        assertThat(post, is(notNullValue()));
        assertThat(post.isLikesOfMe(), is(true));
        Account account = post.getAccount();
        int beforeLikes = post.getLikes();
        assertThat(account, is(notNullValue()));

        post = postService.like(postId, account).orElse(null);
        assertThat(post, is(notNullValue()));
        assertThat(post.isLikesOfMe(), is(true));
        assertThat(post.getLikes(), is(beforeLikes));
    }
}