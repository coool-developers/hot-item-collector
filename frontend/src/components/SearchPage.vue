<script>
import Header from './AppHeader.vue';
import { ref,computed,onMounted } from 'vue';

export default {
  components: {Header},
  setup() {
    const isLoggedIn = ref(false)
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const searchResults = ref([])
    const currentPage = ref(1)
    const totalPages = ref(1)
    const itemsPerPage = 16
    const pageMode = ref('search') // 'search', 'follow', 'category'
    const selectedCategory = ref('')

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

    const pageTitle = computed(() => {
      switch (pageMode.value) {
        case 'search':
          return `(${searchQuery.value}) 제품 검색 결과`
        case 'follow':
          return '팔로우한 사용자의 판매 상품 목록'
        case 'category':
          return selectedCategory.value
        default:
          return '제품 목록'
      }
    })

    const displayedItems = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage
      const end = start + itemsPerPage
      return searchResults.value.slice(start, end)
    })

    const fetchSearchResults = async (page) => {
      try {
        // 실제 API 호출로 대체해야 합니다.
        let url = ''
        if (pageMode.value === 'search') {
          url = `/api/search?query=${searchQuery.value}&type=${searchType.value}&page=${page}&limit=${itemsPerPage}`
        } else if (pageMode.value === 'follow') {
          url = `/api/follow/products?page=${page}&limit=${itemsPerPage}`
        } else if (pageMode.value === 'category') {
          url = `/api/category/${selectedCategory.value}?page=${page}&limit=${itemsPerPage}`
        }
        const response = await fetch(url)
        const data = await response.json()
        searchResults.value = data.items
        totalPages.value = Math.ceil(data.total / itemsPerPage)
      } catch (error) {
        console.error('Failed to fetch results:', error)
      }
    }

    const search = () => {
      pageMode.value = 'search'
      currentPage.value = 1
      fetchSearchResults(currentPage.value)
    }

    const selectCategory = (category) => {
      pageMode.value = 'category'
      selectedCategory.value = category
      currentPage.value = 1
      fetchSearchResults(currentPage.value)
    }

    const showFollowedSellerProducts = () => {
      pageMode.value = 'follow'
      currentPage.value = 1
      fetchSearchResults(currentPage.value)
    }

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--
        fetchSearchResults(currentPage.value)
      }
    }

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++
        fetchSearchResults(currentPage.value)
      }
    }

    const goToItemPage = (itemId) => {
      window.location.href = `/item/${itemId}`
    }

    const goToSellerPage = (userId) => {
      window.location.href = `/seller/${userId}`
    }

    const login = () => {
      console.log('Login clicked')
      // Implement login logic here
      isLoggedIn.value = true
      showLoginModal.value = false
    }

    const register = () => {
      console.log('Register clicked')
      // Implement registration logic here
      isLoggedIn.value = true
      showSignupModal.value = false
    }

    const validateLoginId = () => {
      const loginIdRegex = /^[a-z0-9]{4,10}$/
      if (!loginIdRegex.test(signupLoginId.value)) {
        loginIdError.value = '아이디는 4~10자의 영문 소문자와 숫자만 사용 가능합니다.'
      } else {
        loginIdError.value = ''
      }
    }

    const validatePassword = () => {
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,15}$/
      if (!passwordRegex.test(signupPassword.value)) {
        passwordError.value = '비밀번호는 8~15자의 영문 대/소문자, 숫자, 특수문자를 포함해야 합니다.'
      } else {
        passwordError.value = ''
      }
    }

    const isSignupFormValid = computed(() => {
      return signupLoginId.value && signupPassword.value && username.value && nickname.value && !loginIdError.value && !passwordError.value
    })

    const kakaoLogin = () => {
      console.log('Kakao Login clicked')
      // Implement Kakao login logic here
    }

    const switchToLogin = () => {
      showSignupModal.value = false
      showLoginModal.value = true
    }

    const switchToSignup = () => {
      showLoginModal.value = false
      showSignupModal.value = true
    }

    const goToProductRegistration = () => {
      console.log('Going to product registration')
    }

    const goToProductManagement = () => {
      console.log('Going to product management')
    }

    const goToOrderManagement = () => {
      console.log('Going to order management')
    }

    const viewMyInfo = () => {
      console.log('Going to my info')
    }

    const editProfile = () => {
      console.log('Going to edit profile')
    }

    const logout = () => {
      console.log('Logging out')
      isLoggedIn.value = false
    }

    const deleteAccount = () => {
      if (confirm('정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')) {
        console.log('Deleting account')
        isLoggedIn.value = false
      }
    }

    const goToCart = () => {
      console.log('Going to cart')
    }

    onMounted(() => {
      // 초기 검색 결과를 위한 더미 데이터
      searchResults.value = Array.from({ length: 64 }, (_, i) => ({
        id: i + 1,
        name: `검색 결과 상품 ${i + 1}`,
        image: `https://via.placeholder.com/300x200?text=Item+${i + 1}`,
        userId: Math.floor(Math.random() * 1000) + 1,
        userName: `판매자${Math.floor(Math.random() * 100) + 1}`
      }))
      totalPages.value = Math.ceil(searchResults.value.length / itemsPerPage)
    })

    return {
      isLoggedIn,
      searchType,
      searchQuery,
      categories,
      displayedItems,
      currentPage,
      totalPages,
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
      search,
      selectCategory,
      showFollowedSellerProducts,
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
      goToCart
    }
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
          <div v-for="item in displayedItems" :key="item.id" class="item-card" @click="goToItemPage(item.id)"><img
              :src="item.image" :alt="item.name">
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-seller">
                판매자: <a @click.stop="goToSellerPage(item.userId)" :href="'/seller/' + item.userId">{{ item.userName
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

<style scoped>
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

/* Search Results Styles */
.search-results {
  margin-top: 30px;
  flex-grow: 1;
}

.search-results h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 30px;
  margin-bottom: 30px;
}

.item-card {
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  cursor: pointer;
}

.item-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
}

.item-card img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.item-info {
  padding: 15px;
}

.item-name {
  font-weight: bold;
  margin-bottom: 10px;
  font-size: 16px;
}

.item-seller {
  font-size: 14px;
  color: #666;
}

.item-seller a {
  color: var(--main-color);
  text-decoration: none;
}

.item-seller a:hover {
  text-decoration: underline;
}

/* Pagination Styles */
.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
  margin-bottom: 60px;
}

.pagination button {
  background-color: var(--bg-color);
  color: var(--main-color);
  border: 1px solid var(--main-color);
  padding: 10px 15px;
  margin: 0 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.pagination button:hover {
  background-color: var(--main-color);
  color: var(--bg-color);
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination .page-number {
  font-weight: bold;
  margin: 0 10px;
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