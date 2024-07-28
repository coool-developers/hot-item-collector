<script>
import { ref } from 'vue';

export default {
  setup() {
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const shippingInfo = ref({
      name: '김철수',
      phone: '010-1234-5678',
      address: '서울특별시 강남구 테헤란로 123 아파트 456호'
    })
    const product = ref({
      id: 1,
      name: '수제 초콜릿',
      price: 15000,
      seller: '달콤공방',
      sellerId: 101,
      image: 'https://example.com/chocolate.jpg',
      status: '배송완료'
    })
    const deliveryStatuses = ref(['결제완료', '상품준비중', '배송중', '배송완료'])
    const currentStatusIndex = ref(0) // 현재 상태 (배송완료)

    const search = () => {
      alert(`검색 유형: ${searchType.value}, 검색어: ${searchQuery.value}`)
    }

    const selectCategory = (category) => {
      alert(`선택한 카테고리: ${category}`)
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
      alert('내 정보 보기 페이지로 이동합니다.')
    }

    const editProfile = () => {
      alert('정보 수정 페이지로 이동합니다.')
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

    const goToProductDetail = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
    }

    const goToSellerDetail = (sellerId) => {
      alert(`판매자 ID ${sellerId}의 상세 페이지로 이동합니다.`)
    }

    return {
      searchType,
      searchQuery,
      categories,
      shippingInfo,
      product,
      deliveryStatuses,
      currentStatusIndex,
      search,
      selectCategory,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      goToProductDetail,
      goToSellerDetail
    }
  }
}
</script>

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
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#"
             class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container">
      <div class="order-details">
        <div class="shipping-info">
          <h2>배송지 정보</h2>
          <div class="info-group">
            <label>이름:</label>
            <span>{{ shippingInfo.name }}</span>
          </div>
          <div class="info-group">
            <label>전화번호:</label>
            <span>{{ shippingInfo.phone }}</span>
          </div>
          <div class="info-group">
            <label>주소:</label>
            <span>{{ shippingInfo.address }}</span>
          </div>
        </div>
        <div class="order-summary">
          <h2>주문 상품 정보</h2>
          <div class="delivery-status">
            <h3>배송 상태</h3>
            <div class="status-bar">
              <div v-for="(status, index) in deliveryStatuses" :key="status"
                   :class="['status-step', { active: index <= currentStatusIndex }]">
                <div class="status-icon">{{ index + 1 }}</div>
                <div class="status-label">{{ status }}</div>
              </div>
            </div>
          </div>
          <div class="product-item" @click="goToProductDetail(product.id)">
            <div class="product-content">
              <img :src="product.image" :alt="product.name" class="product-image">
              <div class="product-info">
                <div>
                  <div class="product-name">{{ product.name }}</div>
                  <a :href="'/seller/' + product.sellerId" class="seller-name"
                     @click.stop="goToSellerDetail(product.sellerId)">{{ product.seller }}</a>
                </div>
                <span>{{ product.price.toLocaleString() }}원</span>
              </div>
            </div>
          </div>
        </div>
      </div>
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

<style>
:root {
  --main-color: #FF0000;
  --text-color: #333;
  --bg-color: #FFFFFF;
  --hover-color: #FF6666;
  --button-color: #FF4136;
  --footer-bg: #f8f8f8;
  --card-border: #e0e0e0;
  --input-border: #ccc;
  --section-border: #ddd;
  --status-incomplete: #ccc;
  --status-complete: #4CAF50;
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

/* Main Content Styles */
main {
  flex-grow: 1;
  padding: 60px 0;
}

.order-details {
  display: flex;
  flex-direction: column;
}

.shipping-info,
.order-summary {
  width: 100%;
  margin-bottom: 30px;
  border: 1px solid var(--section-border);
  border-radius: 10px;
  padding: 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
}

h2 {
  font-size: 24px;
  margin-bottom: 20px;
}

.info-group {
  margin-bottom: 15px;
}

.info-group label {
  font-weight: bold;
  display: inline-block;
  width: 100px;
}

.product-item {
  display: flex;
  flex-direction: column;
  padding: 20px;
  border: 1px solid var(--card-border);
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.product-item:hover {
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transform: translateY(-5px);
}

.product-content {
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 20px;
}

.product-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
  flex-grow: 1;
}

.product-name {
  font-weight: bold;
  font-size: 18px;
  margin-bottom: 5px;
}

.seller-name {
  color: var(--main-color);
  text-decoration: none;
  font-weight: bold;
}

.seller-name:hover {
  text-decoration: underline;
}

.delivery-status {
  margin-bottom: 20px;
}

.status-bar {
  display: flex;
  justify-content: space-between;
  margin-top: 10px;
}

.status-step {
  flex: 1;
  text-align: center;
  position: relative;
}

.status-step::before {
  content: '';
  height: 3px;
  width: 100%;
  background-color: var(--status-incomplete);
  position: absolute;
  top: 15px;
  left: 50%;
  z-index: 1;
}

.status-step:last-child::before {
  display: none;
}

.status-step.active::before {
  background-color: var(--status-complete);
}

.status-icon {
  width: 30px;
  height: 30px;
  border-radius: 50%;
  background-color: var(--status-incomplete);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  position: relative;
  z-index: 2;
}

.status-step.active .status-icon {
  background-color: var(--status-complete);
}

.status-label {
  margin-top: 5px;
  font-size: 12px;
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