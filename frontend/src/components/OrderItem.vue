<script setup>
import { ref, onMounted } from 'vue'
// import { ref, computed, onMounted } from 'vue'
import { DateTime } from 'luxon'
import Cookies from "js-cookie";
import axios from "axios";

const searchType = ref('product')
const searchQuery = ref('')
const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
const startDate = ref('')
const endDate = ref('')
const orders = ref([])
const accessToken = Cookies.get('access_token')

// const groupedProducts = computed(() => {
//   const grouped = {}
//   purchasedProducts.value.forEach(product => {
//     const date = product.purchaseDate.split('T')[0]
//     if (!grouped[date]) {
//       grouped[date] = []
//     }
//     grouped[date].push(product)
//   })
//   return grouped
// })

const formatDate = (dateString) => {
  const date = DateTime.fromISO(dateString)
  return date.toFormat('yyyy년 MM월 dd일 (ccc)', { locale: 'ko' })
}

const formatPrice = (price) => {
  return new Intl.NumberFormat('ko-KR').format(price)
}

const searchPurchases = () => {

  axios.get('http://localhost:8080/orders/buy', {
    params: {
      startDate: startDate.value,
      endDate: endDate.value,
    },
    headers: {
      'Authorization': accessToken
    }
  }).then(response => {
    orders.value = response.data.result;
  }).catch(error => {
    console.error(error)
  })
}

onMounted(() => {
  const today = DateTime.now()
  endDate.value = today.toFormat('yyyy-MM-dd')
  startDate.value = today.minus({ months: 3 }).toFormat('yyyy-MM-dd')
  searchPurchases()
})

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
  alert('내 정보 페이지로 이동합니다.')
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

const goToProductDetail = (productId) => {
  alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
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
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#" class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container purchased-products">
      <h1>구매한 상품 목록</h1>
      <div class="date-range-selector">
        <input type="date" v-model="startDate">
        <span>~</span>
        <input type="date" v-model="endDate">
        <button @click="searchPurchases">검색</button>
      </div>
      <div v-for="order in orders" :key="order.id">
        <div class="purchase-date">{{ formatDate(order.createdAt) }}</div>
        <div class="products-container">
          <div v-for="item in order.orderItemResponseDtoList" :key="item.productId" class="product-card" @click="goToProductDetail(item.productId)">
            <img :src="item.productImage.imageUrl" :alt="item.productName" class="product-image">
            <div class="product-info">
              <div class="product-details">
                <div class="product-name">{{ item.productName }}</div>
                <div>
                  판매자: <a :href="'/seller/' + item.sellerId" class="seller-name" @click.stop>{{ item.sellerNickname }}</a>
                </div>
              </div>
              <div class="product-price">{{ formatPrice(item.price) }}원</div>
              <div class="product-status">{{ item.orderStatus }}</div>
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

<style scoped>
:root {
  --main-color: #FF0000;
  --text-color: #333;
  --bg-color: #FFFFFF;
  --hover-color: #FF6666;
  --button-color: #FF4136;
  --footer-bg: #f8f8f8;
  --card-border: #e0e0e0;
  --category-bg: #f1f1f1;
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
  background-color: var(--category-bg);
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

/* Purchased Products Styles */
.purchased-products {
  padding: 20px 0;
}

.date-range-selector {
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 10px;
}

.date-range-selector input[type="date"] {
  padding: 5px;
  border: 1px solid var(--card-border);
  border-radius: 5px;
}

.date-range-selector button {
  padding: 5px 10px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.date-range-selector button:hover {
  background-color: var(--hover-color);
}

.purchase-date {
  font-size: 18px;
  font-weight: bold;
  margin-top: 20px;
  margin-bottom: 10px;
}

.products-container {
  display: flex;
  flex-direction: column;
  gap: 20px;
  margin-bottom: 20px;
}

.product-card {
  border: 1px solid var(--card-border);
  border-radius: 5px;
  padding: 10px;
  transition: box-shadow 0.3s ease;
  display: flex;
  align-items: center;
}

.product-card:hover {
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.product-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 5px;
  margin-right: 20px;
  cursor: pointer;
}

.product-info {
  flex-grow: 1;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.product-details {
  display: flex;
  flex-direction: column;
}

.product-name {
  font-weight: bold;
  font-size: 16px;
  cursor: pointer;
  color: var(--text-color);
  text-decoration: none;
}

.product-name:hover {
  text-decoration: underline;
}

.seller-name {
  color: var(--text-color);
  text-decoration: none;
  font-size: 14px;
}

.seller-name:hover {
  text-decoration: underline;
}

.product-price {
  font-weight: bold;
  font-size: 16px;
  margin-left: auto;
  margin-right: 20px;
}

.product-status {
  font-size: 14px;
  color: #666;
  width: 100px;
  text-align: right;
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