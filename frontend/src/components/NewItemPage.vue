<script>
import Header from './AppHeader.vue';
import { ref, computed, onMounted } from 'vue';
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  components: {Header},
  setup() {
    const isLoggedIn = ref(false);
    const currentPage = ref(1);
    const totalPages = ref(1);
    const itemsPerPage = 16;
    const products = ref([]); // 초기값을 빈 배열로 설정

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
    const router = useRouter();

    const pageTitle = computed(() => '새로 등록된 상품 목록');

    const displayedItems = computed(() => {
      if (!Array.isArray(products.value)) {
        return []; // products가 배열이 아닌 경우 빈 배열 반환
      }
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return products.value.slice(start, end);
    });

    const fetchProducts = async (page) => {
      try {
        const url = `/products/new?page=${page}&size=${itemsPerPage}`;
        const response = await axios.get(url);
        const data = response.data.result;
        products.value = data || [];
        totalPages.value = Math.ceil((data.total || 0) / itemsPerPage);
      } catch (error) {
        console.error('Failed to fetch products:', error);
      }
    };

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchProducts(currentPage.value);
      }
    };

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchProducts(currentPage.value);
      }
    };

    const goToItemPage = (itemId) => {
      window.location.href = `/products/${itemId}`;
    };

    const goToSellerPage = (userId) => {
      window.location.href = `/seller/${userId}`;
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

    const validateLoginId = () => {
      const loginIdRegex = /^[a-z0-9]{4,10}$/;
      if (!loginIdRegex.test(signupLoginId.value)) {
        loginIdError.value = '아이디는 4~10자의 영문 소문자와 숫자만 사용 가능합니다.';
      } else {
        loginIdError.value = '';
      }
    };

    const validatePassword = () => {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/;
      if (!passwordRegex.test(signupPassword.value)) {
        passwordError.value = '비밀번호는 8~15자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.';
      } else {
        passwordError.value = '';
      }
    };

    const isSignupFormValid = computed(() => {
      return signupLoginId.value && signupPassword.value && username.value && nickname.value
          && !loginIdError.value && !passwordError.value;
    });

    const kakaoLogin = () => {
      console.log('Kakao Login clicked');
      // Implement Kakao login logic here
    };

    const switchToLogin = () => {
      showSignupModal.value = false;
      showLoginModal.value = true;
    };

    const switchToSignup = () => {
      showLoginModal.value = false;
      showSignupModal.value = true;
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

    onMounted(() => {
      fetchProducts(currentPage.value);
    });
    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
      router.push(`/product/detail/${productId}`);
    }

    return {
      isLoggedIn,
      currentPage,
      totalPages,
      itemsPerPage,
      products,
      displayedItems,
      pageTitle,
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
      prevPage,
      nextPage,
      goToItemPage,
      goToSellerPage,
      login,
      register,
      validateLoginId,
      validatePassword,
      isSignupFormValid,
      kakaoLogin,
      switchToLogin,
      switchToSignup,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      goToProduct
    };
  }
}
</script>

<template>
  <div id="app">
    <Header/>
    <main class="container">
      <section class="search-results">
        <h2>{{ pageTitle }}</h2>
        <div class="item-grid">
          <div v-for="item in displayedItems" :key="item.id" class="item-card"
               @click="goToProduct(item.id)">
            <img :src="item.image.imageUrl" :alt="item.name">
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-seller">
                판매자: <a @click.stop="goToSellerPage(item.userId)" :href="'/seller/' + item.userId">{{
                  item.userName
                }}</a>
              </div>
            </div>
          </div>
        </div>
        <div class="pagination">
          <button @click="prevPage" :disabled="currentPage === 1">&lt; 이전</button>
          <span class="page-number">{{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">다음 &gt;</button>
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
            <input type="text" id="auth-signupLoginId" v-model="signupLoginId"
                   @input="validateLoginId" required>
            <div class="error" v-if="loginIdError">{{ loginIdError }}</div>
          </div>
          <div class="form-group">
            <label for="auth-signupPassword">비밀번호</label>
            <input type="password" id="auth-signupPassword" v-model="signupPassword"
                   @input="validatePassword" required>
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
        <p>계정이 이미 있으신가요? <a href="#" @click.prevent="switchToLogin">로그인</a></p>
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
          <button type="button" @click="kakaoLogin">카카오 로그인</button>
        </form>
        <p>계정이 없으신가요? <a href="#" @click.prevent="switchToSignup">회원가입</a></p>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* 기본 스타일 추가 */
#app {
  font-family: Arial, sans-serif;
}

.container {
  width: 80%;
  margin: 0 auto;
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4개의 열을 만들고, 각 열이 동일한 비율로 공간을 차지하도록 설정 */
  gap: 16px; /* 아이템 간의 간격을 설정 */
}

.item-card {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;
}

.item-card:hover {
  transform: scale(1.05);
}

.item-card img {
  width: 100%;
  height: auto;
}

.item-info {
  padding: 10px;
}

.item-name {
  font-weight: bold;
}

.item-seller {
  font-size: 0.9em;
  color: #555;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

button:disabled {
  background-color: #ddd;
}

footer {
  background-color: #f8f9fa;
  padding: 20px;
  text-align: center;
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-container {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 400px;
  position: relative;
}

.close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  background: none;
  border: none;
  font-size: 20px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.error {
  color: red;
  font-size: 0.9em;
}
</style>
