<script>
import {onMounted, ref} from 'vue';
import Cookies from "js-cookie";
import axios from "axios";

export default {
  setup() {
    const searchQuery = ref('')
    const searchType = ref('product')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const deliveryStatuses = ref(['결제 완료', '배송 시작', '배송 중', '배송 완료'])
    const statusFilter = ref('')
    const orders = ref([]) // order 상태
    const today = new Date().toISOString().split('T')[0]
    const startDate = ref(new Date(new Date().setMonth(new Date().getMonth() - 3)).toISOString().split('T')[0])
    const endDate = ref(today)
    const accessToken = Cookies.get('access_token')

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

    const searchOrders = () => {
      // API 요청 시뮬레이션
      console.log(`검색 기간: ${startDate.value} ~ ${endDate.value}, 배송상태: ${statusFilter.value}`)
      // 실제 구현에서는 여기에 API 호출 코드가 들어갑니다.
    }

    const loadOrders = () => {
      axios.get('http://localhost:8080/orders/sell', {
        headers: {
          'Authorization' : accessToken
        }
      }).then(response  => {
        orders.value = response.data.result
      }).catch(error => {
        console.error(error)
      })
    }

    const updateStatus = (order) => {
      alert(`주문 #${order.id}의 배송상태가 ${order.orderStatus}(으)로 변경되었습니다.`)
      // 실제 구현에서는 여기에 API 호출 코드가 들어갑니다.
    }

      //
      // // 초기 데이터 로드 (실제 구현에서는 API 호출로 대체)
      // orders.value = [
      //   {
      //     id: 1,
      //     customerName: '김철수',
      //     customerPhone: '010-1234-5678',
      //     customerAddress: '서울시 강남구 테헤란로 123',
      //     productName: '수제 초콜릿',
      //     productPrice: 15000,
      //     productImage: 'https://example.com/chocolate.jpg',
      //     buyerName: '김철수',
      //     status: '결제완료'
      //   },
      //   {
      //     id: 2,
      //     customerName: '이영희',
      //     customerPhone: '010-9876-5432',
      //     customerAddress: '부산시 해운대구 해운대해변로 456',
      //     productName: '핸드메이드 비누',
      //     productPrice: 8000,
      //     productImage: 'https://example.com/soap.jpg',
      //     buyerName: '이영희',
      //     status: '배송중'
      //   },
      //   // 추가 주문 데이터...
      // ]


    onMounted(() => {
      loadOrders()
    })

    return {
      searchQuery,
      searchType,
      categories,
      deliveryStatuses,
      statusFilter,
      orders,
      today,
      startDate,
      endDate,
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
      searchOrders,
      updateStatus
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
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#" class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container">
      <h1>주문 관리</h1>
      <div class="search-filters">
        <input type="date" v-model="startDate" :max="today">
        <input type="date" v-model="endDate" :max="today">
        <select v-model="statusFilter">
          <option value="">전체</option>
          <option v-for="status in deliveryStatuses" :key="status" :value="status">
            {{ status }}
          </option>
        </select>
        <button @click="searchOrders">검색</button>
      </div>
      <div class="order-cards">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="customer-info">
            <p><strong>주문자:</strong> {{ order.userNickname }}</p>
            <p><strong>연락처:</strong> {{ order.phoneNumber }}</p>
            <p><strong>주소:</strong> {{ order.address }}</p>
          </div>
          <div class="order-details">
            <div class="order-info">
              <img :src="order.productImage.imageUrl" :alt="order.productImage.filename" class="product-image">
              <div>
                <p><strong>상품명:</strong> {{ order.productName }}</p>
                <p><strong>가격:</strong> {{ order.price}}원</p>
<!--                <p><strong>구매자:</strong> {{ order.userNickname }}</p>-->
              </div>
            </div>
            <div class="status-update">
              <select v-model="order.orderStatus" class="status-dropdown">
                <option v-for="status in deliveryStatuses" :key="status" :value="status">
                  {{ status }}
                </option>
              </select>
              <button @click="updateStatus(order)" class="update-status">배송상태 변경</button>
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

/* Main Content Styles */
main {
  flex-grow: 1;
  padding: 30px 0;
}

.search-filters {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  margin-bottom: 20px;
  gap: 10px;
}

.search-filters input,
.search-filters select {
  padding: 8px;
  border: 1px solid var(--input-border);
  border-radius: 4px;
}

.search-filters button {
  padding: 8px 15px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.search-filters button:hover {
  background-color: var(--hover-color);
}

.order-cards {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.order-card {
  border: 1px solid var(--card-border);
  border-radius: 8px;
  padding: 15px;
  background-color: var(--bg-color);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.customer-info {
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid var(--card-border);
}

.customer-info p,
.order-info p {
  margin: 5px 0;
}

.order-details {
  display: flex;
  justify-content: space-between;
}

.order-info {
  flex: 1;
  display: flex;
  align-items: center;
}

.product-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  border-radius: 4px;
  margin-right: 15px;
}

.status-update {
  display: flex;
  align-items: center;
  gap: 10px;
}

.status-dropdown {
  padding: 8px;
  border: 1px solid var(--input-border);
  border-radius: 4px;
  width: 150px;
}

.update-status {
  padding: 8px 15px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.update-status:hover {
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
</style>