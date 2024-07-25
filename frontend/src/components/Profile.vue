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
        </div>
      </div>
    </header>

    <nav class="categories">
      <div class="container">
        <div class="categories-container">
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#" class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container my-info">
      <section class="profile-section">
        <img :src="user.profileImage" alt="프로필 이미지" class="profile-image">
        <div class="profile-details">
          <h1 class="profile-name">{{ user.name }}</h1>
          <p class="profile-bio">{{ user.bio }}</p>
          <div class="profile-stats">
            <span>팔로워: {{ user.followers }}</span>
            <button @click="showFollowers">팔로워 목록</button>
          </div>
        </div>
      </section>

      <section class="product-section">
        <h2 @click="goToRegisteredProducts">내가 등록한 상품</h2>
        <div class="product-list">
          <div v-for="product in registeredProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
            </div>
          </div>
        </div>
      </section>

      <section class="product-section">
        <h2 @click="goToPurchasedProducts">내가 구매한 상품</h2>
        <div class="product-list">
          <div v-for="product in purchasedProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
            </div>
          </div>
        </div>
      </section>

      <section class="product-section">
        <h2 @click="goToLikedProducts">내가 좋아요한 상품</h2>
        <div class="product-list">
          <div v-for="product in likedProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
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
  </div>
</template>

<script>
import { ref } from 'vue'

export default {
  name: 'App',
  setup() {
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])

    const user = ref({
      name: '홍길동',
      profileImage: 'https://example.com/profile.jpg',
      bio: '안녕하세요, 핫아이템컬렉터에서 활동 중인 홍길동입니다. 수제 쿠키와 수공예품을 좋아하며, 다양한 핸드메이드 아이템을 수집하고 있습니다. 여러분의 특별한 아이템들을 기대하고 있어요!',
      followers: 1234
    })

    const registeredProducts = ref([
      { id: 'R001', name: '수제 쿠키', image: 'https://example.com/cookie.jpg' },
      { id: 'R002', name: '손뜨개 목도리', image: 'https://example.com/scarf.jpg' },
      { id: 'R003', name: '천연 비누', image: 'https://example.com/soap.jpg' },
      { id: 'R004', name: '수제 캔들', image: 'https://example.com/candle.jpg' }
    ])

    const purchasedProducts = ref([
      { id: 'P001', name: '유기농 잼', image: 'https://example.com/jam.jpg' },
      { id: 'P002', name: '핸드메이드 가방', image: 'https://example.com/bag.jpg' },
      { id: 'P003', name: '아로마 오일', image: 'https://example.com/oil.jpg' },
      { id: 'P004', name: '수제 초콜릿', image: 'https://example.com/chocolate.jpg' }
    ])

    const likedProducts = ref([
      { id: 'L001', name: '수제 마카롱', image: 'https://example.com/macaron.jpg' },
      { id: 'L002', name: '핸드메이드 악세서리', image: 'https://example.com/accessory.jpg' },
      { id: 'L003', name: '친환경 텀블러', image: 'https://example.com/tumbler.jpg' },
      { id: 'L004', name: '수제 도자기', image: 'https://example.com/pottery.jpg' }
    ])

    const showFollowers = () => {
      alert('팔로워 목록 표시 기능은 아직 구현되지 않았습니다.')
    }

    const goToProductRegistration = () => {
      alert('상품 등록 페이지로 이동합니다.')
    }

    const goToProductManagement = () => {
      alert('판매 물품 관리 페이지로 이동합니다.')
    }

    const goToOrderManagement = () => {
      alert('주문 관리 페이지로 이동합니다.')
    }

    const viewMyInfo = () => {
      alert('현재 페이지가 내정보 페이지입니다.')
    }

    const editProfile = () => {
      alert('프로필 수정 페이지로 이동합니다.')
    }

    const logout = () => {
      alert('로그아웃 되었습니다.')
    }

    const deleteAccount = () => {
      const confirmed = confirm('정말로 회원 탈퇴하시겠습니까? 이 작업은 되돌릴 수 없습니다.')
      if (confirmed) {
        alert('회원 탈퇴 처리되었습니다. 이용해 주셔서 감사합니다.')
      }
    }

    const goToCart = () => {
      alert('장바구니 페이지로 이동합니다.')
    }

    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
    }

    const goToRegisteredProducts = () => {
      alert('내가 등록한 상품 목록 페이지로 이동합니다.')
    }

    const goToPurchasedProducts = () => {
      alert('내가 구매한 상품 목록 페이지로 이동합니다.')
    }

    const goToLikedProducts = () => {
      alert('내가 좋아요한 상품 목록 페이지로 이동합니다.')
    }

    return {
      searchType,
      searchQuery,
      categories,
      user,
      registeredProducts,
      purchasedProducts,
      likedProducts,
      showFollowers,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      goToProduct,
      goToRegisteredProducts,
      goToPurchasedProducts,
      goToLikedProducts,
      search() {
        alert(`검색 타입: ${searchType.value}, 검색어: ${searchQuery.value}`)
      },
      selectCategory(category) {
        alert(`카테고리 선택됨: ${category}`)
      }
    }
  }
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

.search-bar select {
  padding: 10px;
  font-size: 16px;
  border: none;
  border-radius: 5px 0 0 5px;
}

.search-bar input {
  padding: 10px;
  font-size: 16px;
  border: none;
  flex-grow: 1;
  min-width: 200px;
}

.search-bar button {
  padding: 10px 20px;
  font-size: 16px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
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
  box-shadow: 0px 8px 16px 0px rgba(0,0,0,0.2);
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
  background-color: #f1f1f1;
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
  width: 250px;
  height: 250px;
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
  color: #000000;
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
  background-color: var(--main-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-size: 16px;
}

.profile-stats button:hover {
  background-color: var(--hover-color);
}

.product-section {
  margin-bottom: 30px;
}

.product-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: #000000;
  cursor: pointer;
  transition: color 0.3s ease;
}

.product-section h2:hover {
  color: var(--main-color);
}

.product-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
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
</style>
