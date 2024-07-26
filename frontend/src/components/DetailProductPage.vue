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
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#"
             class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container">
      <section class="product-detail">
        <div class="seller-info">
          <img :src="product.sellerPhoto" :alt="product.sellerName" class="seller-photo">
          <div class="seller-name-follow">
            <span class="seller-name">{{ product.sellerName }}</span>
            <button v-if="isLoggedIn" class="follow-button" @click="toggleFollow">
              {{ isFollowing ? '팔로우 취소' : '팔로우' }}
            </button>
          </div>
        </div>
        <div class="product-info">
          <div class="product-image-container">
            <img :src="currentImage" :alt="product.name" class="product-image">
            <button class="image-nav-button prev" @click="prevImage" v-if="product.images.length > 1">&lt;</button>
            <button class="image-nav-button next" @click="nextImage" v-if="product.images.length > 1">&gt;</button>
          </div>
          <div class="product-details">
            <div class="product-header">
              <h1 class="product-name">{{ product.name }}</h1>
              <div class="like-actions">
                <span class="like-count">{{ product.likes }} 좋아요</span>
                <button v-if="isLoggedIn" class="like-button" @click="toggleLike">
                  {{ isLiked ? '❤️' : '🤍' }}
                </button>
              </div>
            </div>
            <p class="product-category">{{ product.category }}</p>
            <p class="product-description">{{ product.description }}</p>
            <div class="product-actions-container">
              <p class="product-price">{{ formatPrice(product.price) }}원</p>
              <div class="buy-actions">
                <button class="add-to-cart" @click="addToCart">장바구니 담기</button>
                <button class="buy-now" @click="buyNow">구매하기</button>
              </div>
            </div>
          </div>
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
        <div class="footer-copyright">
          &copy; 2023 Hot Item Collector. All rights reserved.
        </div>
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
import { ref } from 'vue'

export default {
  setup() {
    const isLoggedIn = ref(true)
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const isFollowing = ref(false)
    const isLiked = ref(false)
    const currentImageIndex = ref(0)
    const showLoginModal = ref(false)
    const showSignupModal = ref(false)
    const signupLoginId = ref('')
    const signupPassword = ref('')
    const username = ref('')
    const nickname = ref('')
    const loginId = ref('')
    const password = ref('')
    const loginIdError = ref('')
    const passwordError = ref('')

    // 상품 상세 정보 (실제로는 API에서 가져와야 함)
    const product = ref({
      id: 1,
      name: '수제 도자기 커피 머그컵',
      category: '공예품',
      description: '따뜻한 커피 한 잔을 즐길 수 있는 수제 도자기 머그컵입니다.',
      price: 25000,
      likes: 120,
      images: [
        'https://example.com/image1.jpg',
        'https://example.com/image2.jpg',
        'https://example.com/image3.jpg',
      ],
      sellerName: '홍길동',
      sellerPhoto: 'https://example.com/seller-photo.jpg',
    })

    const currentImage = ref(product.value.images[0])

    const search = () => {
      // 검색 로직
    }

    const toggleFollow = () => {
      isFollowing.value = !isFollowing.value
    }

    const toggleLike = () => {
      isLiked.value = !isLiked.value
      product.value.likes += isLiked.value ? 1 : -1
    }

    const prevImage = () => {
      currentImageIndex.value = (currentImageIndex.value - 1 + product.value.images.length) % product.value.images.length
      currentImage.value = product.value.images[currentImageIndex.value]
    }

    const nextImage = () => {
      currentImageIndex.value = (currentImageIndex.value + 1) % product.value.images.length
      currentImage.value = product.value.images[currentImageIndex.value]
    }

    const addToCart = () => {
      // 장바구니 추가 로직
    }

    const buyNow = () => {
      // 구매 로직
    }

    const formatPrice = (price) => {
      return price.toLocaleString()
    }

    const validateLoginId = () => {
      if (signupLoginId.value.length < 5) {
        loginIdError.value = '아이디는 5자 이상이어야 합니다.'
      } else {
        loginIdError.value = ''
      }
    }

    const validatePassword = () => {
      if (signupPassword.value.length < 8) {
        passwordError.value = '비밀번호는 8자 이상이어야 합니다.'
      } else {
        passwordError.value = ''
      }
    }

    const register = () => {
      // 회원가입 로직
    }

    const login = () => {
      // 로그인 로직
    }

    const kakaoLogin = () => {
      // 카카오 로그인 로직
    }

    const switchToLogin = () => {
      showSignupModal.value = false
      showLoginModal.value = true
    }

    const switchToSignup = () => {
      showLoginModal.value = false
      showSignupModal.value = true
    }

    return {
      isLoggedIn,
      searchType,
      searchQuery,
      categories,
      product,
      currentImage,
      isFollowing,
      isLiked,
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
      toggleFollow,
      toggleLike,
      prevImage,
      nextImage,
      addToCart,
      buyNow,
      formatPrice,
      validateLoginId,
      validatePassword,
      register,
      login,
      kakaoLogin,
      switchToLogin,
      switchToSignup,
    }
  },
}
</script>

<style>
:root {
  --main-color: #FF0000;
  --text-color: #333;
  --bg-color: #FFFFFF;
  --hover-color: #FF6666;
  --button-color: #FF4136;
  --footer-bg: #f8f8f8;
  --kakao-color: #FEE500;
  --input-border: #ccc;
}

body {
  font-family: Arial, sans-serif;
  margin: 0;
  padding: 0;
  background-color: var(--bg-color);
  color: var(--text-color);
  display: flex;
  flex-direction: column;
  min-height: 100vh;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

/* Header Styles */
header {
  background-color: var(--main-color);
  padding: 15px 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.logo {
  font-size: 24px;
  font-weight: bold;
  color: var(--bg-color);
  text-decoration: none;
  margin-right: 20px;
}

.search-bar {
  display: flex;
  align-items: stretch;
  flex-grow: 1;
  margin: 10px 0;
  max-width: 600px;
}

.search-bar select,
.search-bar input,
.search-bar button {
  padding: 10px;
  font-size: 16px;
  border: none;
}

.search-bar select {
  border-radius: 5px 0 0 5px;
}

.search-bar input {
  flex-grow: 1;
  min-width: 200px;
}

.search-bar button {
  background-color: var(--button-color);
  color: var(--bg-color);
  cursor: pointer;
  border-radius: 0 5px 5px 0;
  transition: background-color 0.3s ease;
}

.search-bar button:hover {
  background-color: var(--hover-color);
}

.user-actions {
  display: flex;
  align-items: center;
}

.user-actions button {
  margin-left: 10px;
  padding: 10px 20px;
  background-color: transparent;
  color: var(--bg-color);
  border: 2px solid var(--bg-color);
  cursor: pointer;
  border-radius: 5px;
  font-weight: bold;
  transition: all 0.3s ease;
}

.user-actions button:hover {
  background-color: var(--bg-color);
  color: var(--main-color);
}

/* Categories Styles */
.categories {
  background-color: #FFFFFF;
  padding: 15px 0;
}

.categories-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background-color: var(--bg-color);
  border-radius: 5px;
  overflow: hidden;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.category-item {
  flex: 1;
  text-align: center;
  padding: 15px 0;
  color: var(--text-color);
  text-decoration: none;
  font-weight: bold;
  transition: all 0.3s ease;
  border-right: 1px solid #e0e0e0;
}

.category-item:last-child {
  border-right: none;
}

.category-item:hover {
  background-color: var(--hover-color);
  color: var(--bg-color);
}

/* Product Detail Styles */
.product-detail {
  display: flex;
  flex-direction: column;
  padding: 30px 0;
}

.seller-info {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.seller-photo {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 15px;
}

.seller-name-follow {
  display: flex;
  align-items: center;
  gap: 15px;
}

.seller-name {
  font-size: 18px;
  font-weight: bold;
}

.follow-button {
  padding: 5px 15px;
  background-color: var(--main-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.follow-button:hover {
  background-color: var(--hover-color);
}

.product-info {
  display: flex;
  gap: 30px;
}

.product-image-container {
  flex: 1;
  max-width: 500px;
  position: relative;
  aspect-ratio: 1 / 1;
  overflow: hidden;
}

.product-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 8px;
}

.image-nav-button {
  position: absolute;
  top: 50%;
  transform: translateY(-50%);
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  border: none;
  padding: 10px;
  cursor: pointer;
  font-size: 20px;
  transition: background-color 0.3s ease;
}

.image-nav-button:hover {
  background-color: rgba(0, 0, 0, 0.7);
}

.image-nav-button.prev {
  left: 10px;
}

.image-nav-button.next {
  right: 10px;
}

.product-details {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.product-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.product-name {
  font-size: 24px;
  font-weight: bold;
}

.like-actions {
  display: flex;
  align-items: center;
}

.like-count {
  font-size: 16px;
  margin-right: 10px;
}

.like-button {
  background-color: transparent;
  border: none;
  cursor: pointer;
  font-size: 24px;
  padding: 0;
}

.product-category {
  font-size: 16px;
  color: #666;
  margin-bottom: 20px;
}

.product-description {
  font-size: 16px;
  line-height: 1.6;
  margin-bottom: 20px;
  flex-grow: 1;
}

.product-actions-container {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.product-price {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 15px;
  color: var(--main-color);
  text-align: right;
}

.buy-actions {
  display: flex;
  gap: 10px;
}

.add-to-cart,
.buy-now {
  padding: 12px 24px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  flex: 1;
}

.add-to-cart {
  background-color: #f0f0f0;
  color: var(--text-color);
}

.add-to-cart:hover {
  background-color: #e0e0e0;
}

.buy-now {
  background-color: var(--main-color);
  color: var(--bg-color);
}

.buy-now:hover {
  background-color: var(--hover-color);
}

/* Footer Styles */
footer {
  background-color: var(--footer-bg);
  padding: 30px 0;
  margin-top: auto;
}

.footer-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
}

.footer-links {
  display: flex;
  gap: 20px;
}

.footer-links a {
  color: var(--text-color);
  text-decoration: none;
  transition: color 0.3s ease;
}

.footer-links a:hover {
  color: var(--main-color);
}

.footer-copyright {
  margin-top: 20px;
  text-align: center;
  width: 100%;
  font-size: 14px;
  color: #666;
}

/* Modal Styles */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-container {
  background-color: var(--bg-color);
  padding: 30px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
  max-width: 400px;
  width: 90%;
  max-height: 90vh;
  overflow-y: auto;
  position: relative;
}

.modal-container .close-btn {
  position: absolute;
  top: 10px;
  right: 10px;
  font-size: 24px;
  cursor: pointer;
  background: none;
  border: none;
  color: var(--text-color);
  padding: 0;
  width: auto;
}

.modal-container .close-btn:hover {
  color: var(--main-color);
}

.modal-container h1 {
  text-align: center;
  color: var(--text-color);
  margin-bottom: 20px;
}

.modal-container .form-group {
  margin-bottom: 20px;
}

.modal-container label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.modal-container input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--input-border);
  border-radius: 4px;
  font-size: 16px;
  box-sizing: border-box;
}

.modal-container .error {
  color: var(--main-color);
  font-size: 14px;
  margin-top: 5px;
}

.modal-overlay button {
  width: 100%;
  padding: 10px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 4px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.modal-overlay button:hover {
  background-color: var(--hover-color);
}

.modal-overlay button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.modal-container .social-login {
  margin-top: 20px;
  text-align: center;
}

.modal-container .social-login-divider {
  display: flex;
  align-items: center;
  margin: 15px 0;
}

.modal-container .social-login-divider::before,
.modal-container .social-login-divider::after {
  content: "";
  flex: 1;
  border-bottom: 1px solid var(--input-border);
}

.modal-container .social-login-divider span {
  padding: 0 10px;
  color: var(--text-color);
  font-size: 14px;
}

.modal-container .kakao-login-btn {
  background-color: var(--kakao-color);
  color: #3C1E1E;
  font-weight: bold;
}

.modal-container .kakao-login-btn:hover {
  background-color: #E6D100;
}

.login-link,
.signup-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.login-link a,
.signup-link a {
  color: var(--main-color);
  text-decoration: none;
  cursor: pointer;
}

.login-link a:hover,
.signup-link a:hover {
  text-decoration: underline;
}

/* Dropdown Menu Styles */
.dropdown {
  position: relative;
  display: inline-block;
}

.dropdown-content {
  display: none;
  position: absolute;
  right: 0;
  background-color: var(--bg-color);
  min-width: 160px;
  box-shadow: 0px 8px 16px 0px rgba(0, 0, 0, 0.2);
  z-index: 1;
  border-radius: 5px;
}

.dropdown-content a {
  color: var(--text-color);
  padding: 12px 16px;
  text-decoration: none;
  display: block;
  transition: background-color 0.3s ease;
}

.dropdown-content a:hover {
  background-color: var(--hover-color);
  color: var(--bg-color);
}

.dropdown:hover .dropdown-content {
  display: block;
}
</style>
