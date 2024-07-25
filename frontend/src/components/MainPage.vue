<template>
  <div id="app">
    <header>
      <div class="container header-content">
        <a href="/" class="logo">Hot Item Collector</a>
        <div class="search-bar">
          <select v-model="searchType">
            <option value="product">상품명</option>
            <option value="seller">판매자명</option>
          </select>
          <input type="text" v-model="searchQuery" placeholder="검색어를 입력하세요">
          <button @click="search">검색</button>
        </div>
        <div class="user-actions">
          <template v-if="isLoggedIn">
            <div class="dropdown">
              <button>상품</button>
              <div class="dropdown-content">
                <a href="#" @click="goToProductRegistration">상품 등록</a>
                <a href="#" @click="goToProductManagement">판매 물품 관리</a>
                <a href="#" @click="goToOrderManagement">주문 관리</a>
              </div>
            </div>
            <div class="dropdown">
              <button>내정보</button>
              <div class="dropdown-content">
                <a href="#" @click="viewMyInfo">내정보 보기</a>
                <a href="#" @click="editProfile">정보 수정</a>
                <a href="#" @click="logout">로그아웃</a>
                <a href="#" @click="deleteAccount">회원 탈퇴</a>
              </div>
            </div>
            <button @click="goToCart">장바구니</button>
          </template>
          <template v-else>
            <button @click="showLoginModal = true">로그인</button>
            <button @click="showSignupModal = true">회원가입</button>
          </template>
        </div>
      </div>
    </header>
    <nav class="categories">
      <div class="container">
        <div class="categories-container">
          <a v-for="category in categories" :key="category" :href="'/category/' + category" class="category-item">{{ category }}</a>
        </div>
      </div>
    </nav>
    <main class="container">
      <section class="hot-top-10">
        <h2>Hot Top 10 Items</h2>
        <ol>
          <li v-for="item in hotTopItems" :key="item.id"><a :href="'/item/' + item.id">{{ item.name }}</a></li>
        </ol>
      </section>
      <section class="new-items">
        <h2>새로 등록된 상품</h2>
        <div v-if="newItems.length > 0" class="item-cards">
          <div v-for="item in newItems" :key="item.id" class="item-card">
            <img :src="item.image" :alt="item.name">
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="seller-info">판매자: <a :href="'/seller/' + item.userId">{{ item.userName }}</a></div>
            </div>
          </div>
        </div>
        <div v-else class="no-items-message">등록된 상품이 없습니다.</div>
        <div v-if="newItems.length > 0" class="view-more-container">
          <button @click="viewMoreNewItems" class="view-more-btn">상품 더보기</button>
        </div>
      </section>
      <section v-if="isLoggedIn" class="followed-users-items">
        <h2>팔로우한 사용자의 상품</h2>
        <div v-if="followedUsersItems.length > 0" class="item-cards">
          <div v-for="item in followedUsersItems" :key="item.id" class="item-card">
            <img :src="item.image" :alt="item.name">
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="seller-info">판매자: <a :href="'/seller/' + item.userId">{{ item.userName }}</a></div>
            </div>
          </div>
        </div>
        <div v-else class="no-items-message">팔로우한 사용자의 상품이 없습니다.</div>
        <div v-if="followedUsersItems.length > 0" class="view-more-container">
          <button @click="viewMoreFollowedItems" class="view-more-btn">상품 더보기</button>
        </div>
      </section>
    </main>
    <footer>
      <div class="container footer-content">
        <div class="footer-links">
          <a href="/about">회사 소개</a>
          <a href="/terms">이용약관</a>
          <a href="/privacy">개인정보처리방침</a>
          <a href="/contact">고객센터</a>
        </div>
        <div class="footer-copyright">&copy; 2023 Hot Item Collector. All rights reserved.</div>
      </div>
    </footer>
    <!-- 회원가입 모달 -->
    <div v-if="showSignupModal" class="modal-overlay" @click.self="showSignupModal = false">
      <div class="modal-container">
        <button class="close-btn" @click="showSignupModal = false">&times;</button>
        <h1>회원가입</h1>
        <form @submit.prevent="register">
          <div class="form-group">
            <label for="auth-signupLoginId">아이디</label>
            <input type="text" id="auth-signupLoginId" v-model="signupLoginId" @input="validateLoginId" required>
            <div class="error" v-if="loginIdError">{{ loginIdError }}</div>
          </div>
          <div class="form-group">
            <label for="auth-signupPassword">비밀번호</label>
            <input type="password" id="auth-signupPassword" v-model="signupPassword" @input="validatePassword" required>
            <div class="error" v-if="passwordError">{{ passwordError }}</div>
          </div>
          <div class="form-group">
            <label for="auth-username">이름</label>
            <input type="text" id="auth-username" v-model="username" required>
          </div>
          <div class="form-group">
            <label for="auth-nickname">닉네임</label>
            <input type="text" id="auth-nickname" v-model="nickname" required>
          </div>
          <button type="submit" :disabled="!isSignupFormValid">회원가입</button>
        </form>
        <div class="social-login">
          <div class="social-login-divider">
            <span>또는</span>
          </div>
          <button @click="kakaoLogin" class="kakao-login-btn">카카오톡으로 회원가입</button>
        </div>
        <div class="login-link">
          이미 계정이 있으신가요? <a @click="switchToLogin">로그인</a>
        </div>
      </div>
    </div>
    <!-- 로그인 모달 -->
    <div v-if="showLoginModal" class="modal-overlay" @click.self="showLoginModal = false">
      <div class="modal-container">
        <button class="close-btn" @click="showLoginModal = false">&times;</button>
        <h1>로그인</h1>
        <form @submit.prevent="login">
          <div class="form-group">
            <label for="auth-loginId">아이디</label>
            <input type="text" id="auth-loginId" v-model="loginId" required>
          </div>
          <div class="form-group">
            <label for="auth-password">비밀번호</label>
            <input type="password" id="auth-password" v-model="password" required>
          </div>
          <button type="submit">로그인</button>
        </form>
        <div class="social-login">
          <div class="social-login-divider">
            <span>또는</span>
          </div>
          <button @click="kakaoLogin" class="kakao-login-btn">카카오톡으로 로그인</button>
        </div>
        <div class="signup-link">
          계정이 없으신가요? <a @click="switchToSignup">회원가입</a>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed } from 'vue';

export default {
  setup() {
    const isLoggedIn = ref(false);
    const searchType = ref('product');
    const searchQuery = ref('');
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물']);
    const hotTopItems = ref([
      { id: 1, name: '초특가 스마트폰' },
      { id: 2, name: '인기 노트북' },
      { id: 3, name: '베스트셀러 도서' },
    ]);
    const newItems = ref([
      { id: 101, name: '새 상품 1', image: 'https://via.placeholder.com/250x200', userId: 1001, userName: '판매자1' },
    ]);
    const followedUsersItems = ref([
      { id: 201, name: '팔로우 상품 1', image: 'https://via.placeholder.com/250x200', userId: 2001, userName: '팔로우판매자1' },
    ]);

    const showLoginModal = ref(false);
    const showSignupModal = ref(false);
    const signupLoginId = ref('');
    const signupPassword = ref('');
    const username = ref('');
    const nickname = ref('');
    const loginId = ref('');
    const password = ref('');
    const loginIdError = ref('');
    const passwordError = ref('');

    const search = () => {
      console.log(`Searching for ${searchQuery.value} in ${searchType.value}`);
    };

    const login = () => {
      console.log('Login clicked');
      isLoggedIn.value = true;
      showLoginModal.value = false;
    };

    const register = () => {
      console.log('Register clicked');
      isLoggedIn.value = true;
      showSignupModal.value = false;
    };

    const goToProductRegistration = () => {
      console.log('Going to product registration');
    };

    const goToProductManagement = () => {
      console.log('Going to product management');
    };

    const goToOrderManagement = () => {
      console.log('Going to order management');
    };

    const viewMyInfo = () => {
      console.log('Going to my info');
    };

    const editProfile = () => {
      console.log('Going to edit profile');
    };

    const logout = () => {
      console.log('Logging out');
      isLoggedIn.value = false;
    };

    const deleteAccount = () => {
      if (confirm('정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        console.log('Deleting account');
        isLoggedIn.value = false;
      }
    };

    const goToCart = () => {
      console.log('Going to cart');
    };

    const viewMoreNewItems = () => {
      console.log('View more new items');
      window.location.href = '/new-items';
    };

    const viewMoreFollowedItems = () => {
      console.log('View more followed items');
      window.location.href = '/followed-items';
    };

    const validateLoginId = () => {
      const loginIdRegex = /^[a-z0-9]{4,10}$/;
      if (!loginIdRegex.test(signupLoginId.value)) {
        loginIdError.value = '아이디는 4~10자의 영문 소문자와 숫자만 사용 가능합니다.';
      } else {
        loginIdError.value = '';
      }
    };

    const validatePassword = () => {
      if (signupPassword.value.length < 6) {
        passwordError.value = '비밀번호는 최소 6자 이상이어야 합니다.';
      } else {
        passwordError.value = '';
      }
    };

    const isSignupFormValid = computed(() => {
      return signupLoginId.value && signupPassword.value && username.value && nickname.value && !loginIdError.value && !passwordError.value;
    });

    const switchToLogin = () => {
      showSignupModal.value = false;
      showLoginModal.value = true;
    };

    const switchToSignup = () => {
      showLoginModal.value = false;
      showSignupModal.value = true;
    };

    const kakaoLogin = () => {
      console.log('Kakao login clicked');
    };

    return {
      isLoggedIn,
      searchType,
      searchQuery,
      categories,
      hotTopItems,
      newItems,
      followedUsersItems,
      showLoginModal,
      showSignupModal,
      signupLoginId,
      signupPassword,
      username,
      nickname,
      loginId,
      password,
      loginIdError,
      passwordError,
      search,
      login,
      register,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      viewMoreNewItems,
      viewMoreFollowedItems,
      validateLoginId,
      validatePassword,
      isSignupFormValid,
      switchToLogin,
      switchToSignup,
      kakaoLogin,
    };
  },
};
</script>

<style scoped>
/* Add your styles here */
.container {
  width: 80%;
  margin: 0 auto;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1em 0;
}

.logo {
  font-size: 1.5em;
  text-decoration: none;
}

.search-bar {
  display: flex;
  align-items: center;
}

.search-bar select, .search-bar input {
  margin-right: 0.5em;
}

.user-actions {
  display: flex;
  align-items: center;
}

.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  background-color: #f9f9f9;
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
  z-index: 1;
}

.dropdown:hover .dropdown-content {
  display: block;
}

.item-cards {
  display: flex;
  flex-wrap: wrap;
}

.item-card {
  width: 250px;
  margin: 1em;
}

.item-card img {
  width: 100%;
}

.footer-content {
  text-align: center;
  padding: 1em 0;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
}

.modal-container {
  background: #fff;
  padding: 2em;
  border-radius: 8px;
  width: 400px;
  max-width: 100%;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 1em;
  right: 1em;
  background: none;
  border: none;
  font-size: 1.5em;
  cursor: pointer;
}

.social-login-divider {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 1em 0;
}

.social-login-divider span {
  background: #fff;
  padding: 0 1em;
}

.kakao-login-btn {
  background: #F7E300;
  border: none;
  padding: 0.5em 1em;
  cursor: pointer;
}

.error {
  color: red;
  font-size: 0.875em;
}

.view-more-btn {
  background: #007BFF;
  color: #fff;
  border: none;
  padding: 0.5em 1em;
  cursor: pointer;
}

.view-more-container {
  text-align: center;
}

.signup-link, .login-link {
  text-align: center;
  margin-top: 1em;
}

.signup-link a, .login-link a {
  color: #007BFF;
  cursor: pointer;
}
</style>
