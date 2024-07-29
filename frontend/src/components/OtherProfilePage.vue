<script>
import {ref, computed, onMounted} from 'vue';
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import {useRoute, useRouter} from "vue-router";
import axios from "axios";

export default {
  components: {AppFooter, AppHeader},
  setup() {
    const isLoggedIn = ref(false)
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
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
    const route = useRoute();
    const userId = route.params.userId;
    const router = useRouter();

    const user = ref({
      id: '',
      name: '',
      profileImage: '',
      bio: '',
      followers: 0
    });

    const fetchUser = async () => {
      if(userId){
        const response = await axios.get(`http://localhost:8080/users/profile/${userId}`);
        console.log(response.data.result);

        user.value = response.data.result;
      }else {
        console.error('User Id is missing in route parameters');
      }
    }

    onMounted(fetchUser);
    const products = ref([])

    const fetchProduct = async () => {
      if(userId){
        const response = await axios.get(`http://localhost:8080/products/sale/${userId}`,{
          params: {
            page: 1,
            size: 4
          }
        });
        console.log(response.data.result);

        products.value = response.data.result.map(product =>({
          id: product.id,
          name: product.name,
          image: product.image.imageUrl,
          status:product.status
        }));
      }
    }
    onMounted(fetchProduct);

    const itemsPerPage = 12
    const currentPage = ref(1)

    const totalPages = computed(() => Math.ceil(products.value.length / itemsPerPage))

    const displayedProducts = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage
      const end = start + itemsPerPage
      return products.value.slice(start, end)
    })

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--
      }
    }

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++
      }
    }

    const search = () => {
      console.log(`Searching for ${searchQuery.value} in ${searchType.value}`)
    }

    const selectCategory = (category) => {
      console.log(`Selected category: ${category}`)
    }

    const followUser = () => {
      alert('팔로우 기능은 아직 구현되지 않았습니다.')
    }

    const viewMyInfo = () => {
      alert('현재 페이지가 내정보 페이지입니다.')
    }

    const editProfile = () => {
      alert('프로필 수정 페이지로 이동합니다.')
    }

    const logout = () => {
      alert('로그아웃 되었습니다.')
      isLoggedIn.value = false
    }

    const deleteAccount = () => {
      const confirmed = confirm('정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')
      if (confirmed) {
        alert('회원 탈퇴 처리되었습니다. 이용해 주셔서 감사합니다.')
        isLoggedIn.value = false
      }
    }

    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
      router.push(`/product/detail/${productId}`)
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

    const goToCart = () => {
      console.log('Going to cart')
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

    return {
      isLoggedIn,
      searchType,
      searchQuery,
      categories,
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
      user,
      displayedProducts,
      currentPage,
      totalPages,
      prevPage,
      nextPage,
      search,
      selectCategory,
      followUser,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToProduct,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      goToCart,
      login,
      register,
      validateLoginId,
      validatePassword,
      isSignupFormValid,
      kakaoLogin,
      switchToLogin,
      switchToSignup
    }
  }
}
</script>

<template>
  <div id="app">
    <AppHeader/>
    <main class="container my-info">
      <section class="profile-section">
        <img :src="user.profileImage.imageUrl" alt="프로필 이미지" class="profile-image">
        <div class="profile-details">
          <h1 class="profile-name">{{ user.nickname }}</h1>
          <p class="profile-bio">{{ user.info }}</p>
          <div class="profile-stats">
            <span>팔로워: {{ user.followers }}</span>
            <button @click="followUser">팔로우</button>
          </div>
        </div>
      </section>

      <section class="product-section">
        <h2>판매 상품</h2>
        <div class="product-list">
          <div v-for="product in displayedProducts" :key="product.id" class="product-card"
               @click="goToProduct(product.id)">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
            </div>
          </div>
        </div>
        <div class="pagination">
          <button @click="prevPage" :disabled="currentPage === 1">이전</button>
          <span>{{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
        </div>
      </section>
    </main>
   <AppFooter/>
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

/* My Info Styles */
.my-info {
  padding: 30px 0;
}

.profile-section {
  display: flex;
  align-items: flex-start;
  margin-bottom: 50px;
  padding: 30px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.profile-image {
  width: 200px;
  height: 200px;
  border-radius: 50%;
  object-fit: cover;
  margin-right: 30px;
}

.profile-details {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.profile-name {
  font-size: 32px;
  font-weight: bold;
  margin-bottom: 10px;
  color: var(--text-color);
}

.profile-bio {
  font-size: 18px;
  margin-bottom: 20px;
  max-width: 600px;
  line-height: 1.6;
}

.profile-stats {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.profile-stats span {
  margin-right: 20px;
  font-size: 18px;
  font-weight: bold;
}

.profile-stats button {
  padding: 10px 20px;
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
}

.profile-stats button:hover {
  background-color: var(--hover-color);
  color: var(--bg-color);
}

.product-section {
  margin-bottom: 30px;
}

.product-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: var(--text-color);
}

.product-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
}

.product-card {
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  overflow: hidden;
  transition: box-shadow 0.3s ease;
  cursor: pointer;
}

.product-card:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.product-image {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.product-info {
  padding: 15px;
}

.product-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.product-id {
  color: #666;
  font-size: 14px;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  padding: 10px 20px;
  margin: 0 5px;
  background-color: var(--main-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination button:hover {
  background-color: var(--hover-color);
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
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
</style>