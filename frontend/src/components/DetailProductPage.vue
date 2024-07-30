<template>
  <div id="app">
   <Header/>

    <main class="container">
      <section class="product-detail">
        <div class="seller-info">
          <img :src="product.profileImage.imageUrl || defaultProfileImage" alt="Profile Image" width="70px">
          <div class="seller-name-follow">
            <span class="seller-name">{{ product.nickname }}</span>
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
            <p class="product-description">{{ product.info }}</p>
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
    <AppFooter/>
  </div>
<!--카트 모달-->
  <div v-if="showCartModal" class="modal-overlay" @click.self="showCartModal = false">
    <div class="modal-container">
      <button class="close-btn" @click="showCartModal = false">&times;</button>
      <h1>{{ buttonText }}</h1>
      <button @click="goToCart">장바구니로 이동하기</button>
    </div>
  </div>

</template>

<script>
import Header from './AppHeader.vue';
import {ref, onMounted, computed} from 'vue'
import axios from "axios";
import {useRoute} from "vue-router";
import AppFooter from "@/components/AppFooter.vue";
import defaultUserImage from "../assets/user.png"
import Cookies from "js-cookie";
import router from "@/router";

export default {
  components: {AppFooter, Header},
  props: {
    productId: {
      type: String,
      required: true
    }
  },
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
    const route = useRoute(); // useRoute를 통해 현재 라우트에 접근
    const productId = route.params.productId; // 라우트 파라미터에서 productId를 가져옴
    const accessToken = Cookies.get('access_token')
    const showCartModal = ref(false);
    const cartExist = ref(false)
    const buttonText = computed(() => {
      return cartExist.value ? '선택한 상품이 이미 장바구니에 담겨있습니다.' : '선택한 상품을 장바구니에 담았습니다.';
    });

    // 기본 프로필 이미지 URL
    const defaultProfileImage = defaultUserImage;

    // Product 초기화
    const product = ref({
      id: null,
      name: '',
      category: '',
      images: [],
      price: 0,
      info: '',
      likes: 0,
      profileImage: {
        id: null,
        filename: '',
        imageUrl: defaultProfileImage
      },
      userId: null
    });

    const fetchProduct = async () => {
      try {
        const response = await axios.get(`/products/${productId}`, {
          headers: {
            'Content-Type': 'application/json',
          },
        });

        // product 데이터 설정
        product.value = response.data.result;

        // 프로필 이미지가 null일 경우 기본 이미지 설정
        if (!product.value.profileImage || !product.value.profileImage.imageUrl) {
          product.value.profileImage = {
            id: null,
            filename: '',
            imageUrl: defaultProfileImage
          };
        }
      } catch (error) {
        console.error('Error fetching product:', error);
      }
    };

    const fetchLikeStatus = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/likes/${productId}`, {
          headers: {
            'Authorization': accessToken
          }
        });
        isLiked.value = response.data.result.userLike
      } catch(error) {
        console.error(error)
      }
    }

    const fetchFollowStatus = async () => {
      try {
        await fetchProduct()
        const response = await axios.get(`http://localhost:8080/follow/${product.value.userId}`, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = response.data.result.userFollow
        console.log("팔로우 여부 불러오기 완료")
      } catch(error) {
        console.error(error)
      }
    }

    onMounted(() => {
      fetchProduct();
      fetchLikeStatus();
      fetchFollowStatus();
    });

    const currentImage = computed(() => {
      return product.value.images.length > 0 ? product.value.images[currentImageIndex.value].imageUrl : '';
    });

    const search = () => {
      // 검색 로직
    }

    const follow = async () => {
      try {
        await axios.post(`http://localhost:8080/follow/${product.value.userId}`, {}, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = true;
        console.log('팔로우 성공')
      } catch (error) {
        console.error('팔로우 실패:', error);
      }
    };

    const unfollow = async () => {
      try {
        await axios.delete(`http://localhost:8080/follow/${product.value.userId}`, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = false;
        console.log('팔로우 취소 성공');
      } catch (error) {
        console.error('팔로우 취소 실패:', error);
      }
    };

    const toggleFollow = async () => {
      if (isFollowing.value) {
        await unfollow();
      } else {
        await follow();
      }
    }

    const toggleLike = async () => {

      try {
        await axios.post(`http://localhost:8080/likes/${productId}`, {}, {
          headers: {
            'Authorization': accessToken
          }
        });
        isLiked.value = !isLiked.value;
        product.value.likes += isLiked.value ? 1 : -1;
      } catch (error) {
        console.error('Error toggling like:', error);
      }
     }

    const prevImage = () => {
      currentImageIndex.value = (currentImageIndex.value - 1 + product.value.images.length) % product.value.images.length
      currentImage.value = product.value.images[currentImageIndex.value]
    }

    const nextImage = () => {
      currentImageIndex.value = (currentImageIndex.value + 1) % product.value.images.length
      currentImage.value = product.value.images[currentImageIndex.value]
    }

    const goToCart = () => {
      router.push({name: 'CartPage'})
    }

    const addToCart = () => {

      axios.post(`http://localhost:8080/cart/${productId}`, {},{
        headers: {
          'Content-Type':'application/json',
          'Authorization':accessToken
        },
      }).then(response => {
        console.log(response)
        cartExist.value = false
        showCartModal.value = true

      }).catch(error => {
        cartExist.value = true
        showCartModal.value = true

        console.error(error); // 에러 처리
      })
    }

    const buyNow = async () => {
      await fetchProduct();
      // 장바구니에 값이 이미 있으면 409 에러, 확인 필요
      axios.post(`http://localhost:8080/cart/${productId}`, {},{
        headers: {
          'Content-Type':'application/json',
          'Authorization':accessToken
        },
      })

      const orderData = {
        id: product.value.id,
        productName: product.value.name,
        price: product.value.price,
        seller: product.value.nickname,
        productImage: product.value.images[0]
      };

     // const orderData = response
      sessionStorage.setItem('orderData', JSON.stringify([orderData]))
      router.push({name: 'OrderPage'})
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

    const switchToCart = () => {
      showCartModal.value = false
      showCartModal.value = true
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
      showCartModal,
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
      goToCart,
      buyNow,
      formatPrice,
      validateLoginId,
      validatePassword,
      register,
      login,
      kakaoLogin,
      switchToLogin,
      switchToSignup,
      switchToCart,
      buttonText
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
.signup-link,
.cart-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
}

.login-link a,
.signup-link a,
.cart-link a {
  color: var(--main-color);
  text-decoration: none;
  cursor: pointer;
}

.login-link a:hover,
.signup-link a:hover
.cart-link a:hover {
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
