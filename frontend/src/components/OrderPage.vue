<script>
import {ref, computed, onMounted} from 'vue';

export default {
  setup() {
    const searchQuery = ref('')
    const searchType = ref('product')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const shippingInfo = ref({
      name: '',
      phone: '',
      address: ''
    })
    const cartItems = ref([])

    const loadCartItems = () => {
      // 아까 저장했던 정보들이 storedData로 들어옴.
      const storedData = sessionStorage.getItem('orderData')
      const orderDataArray = JSON.parse(storedData)

      orderDataArray.forEach(orderData => {
        cartItems.value.push({
          id: orderData.id,
          productName: orderData.productName,
          price: orderData.price,
          seller: orderData.seller,
          productImage: orderData.productImage
        })
      })
    }

    const totalAmount = computed(() => {
      return cartItems.value.reduce((total, item) => total + item.price, 0)
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

    const useMyAddress = async () => {
      try {
        // API를 통해 주소 정보를 가져오는 것을 시뮬레이션
        const response = await new Promise(resolve => {
          setTimeout(() => {
            resolve({
              name: '김철수',
              phone: '010-1234-5678',
              address: '서울특별시 강남구 테헤란로 123 아파트 456호'
            })
          }, 1000)
        })

        shippingInfo.value = response
        alert('내 주소 정보를 불러왔습니다.')
      } catch (error) {
        alert('주소 정보를 불러오는데 실패했습니다.')
      }
    }

    const checkout = () => {
      if (!shippingInfo.value.name || !shippingInfo.value.phone || !shippingInfo.value.address) {
        alert('배송지 정보를 모두 입력해주세요.')
        return
      }
      alert('결제가 완료되었습니다. 감사합니다!')
    }

    onMounted(() => {
      loadCartItems(); // 컴포넌트가 마운트될 때 API 호출
    });

    return {
      searchQuery,
      searchType,
      categories,
      shippingInfo,
      cartItems,
      totalAmount,
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
      useMyAddress,
      checkout
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
      <div class="order-details">
        <div class="shipping-info">
          <h2>
            배송지 입력
            <button class="use-my-address" @click="useMyAddress">내 주소 배송</button>
          </h2>
          <div class="input-group">
            <label for="name">이름</label>
            <input type="text" id="name" v-model="shippingInfo.name">
          </div>
          <div class="input-group">
            <label for="phone">전화번호</label>
            <input type="tel" id="phone" v-model="shippingInfo.phone">
          </div>
          <div class="input-group">
            <label for="address">주소</label>
            <input type="text" id="address" v-model="shippingInfo.address">
          </div>
        </div>
        <div class="order-summary">
          <h2>주문 상품 정보</h2>
          <div v-for="product in cartItems" :key="product.id" class="product-item">
            <img :src="product.productImage.imageUrl" :alt="product.productName" class="product-image">
            <div class="product-info">
              <h3>{{ product.productName }}</h3>
              <p>가격: {{ product.price }}원</p>
              <p>판매자: {{ product.seller }}</p>
            </div>
          </div>
          <div class="total-amount">
            <!--             최종 결제금액: {{ totalAmount.toLocaleString() }}원-->
            최종 결제금액: {{ totalAmount }}원
          </div>
          <button class="checkout-button" @click="checkout">결제하기</button>
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
  padding: 60px 0; /* 상하 패딩 더 증가 */
}

.order-details {
  display: flex;
  flex-direction: column;
}

.shipping-info, .order-summary {
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
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.input-group {
  margin-bottom: 15px;
}

.input-group label {
  display: block;
  margin-bottom: 5px;
}

.input-group input {
  width: 100%;
  padding: 10px;
  border: 1px solid var(--input-border);
  border-radius: 5px;
}

.use-my-address {
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  padding: 10px 15px;
  cursor: pointer;
  border-radius: 5px;
  font-weight: bold;
  transition: background-color 0.3s ease;
}

.use-my-address:hover {
  background-color: var(--hover-color);
}

.product-item {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid var(--card-border);
  border-radius: 5px;
}

.product-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 20px;
}

.product-info h3 {
  margin: 0 0 10px 0;
}

.total-amount {
  font-size: 20px;
  font-weight: bold;
  margin-top: 20px;
  text-align: right;
}

.checkout-button {
  display: block;
  width: 100%;
  padding: 15px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  font-size: 18px;
  font-weight: bold;
  cursor: pointer;
  transition: background-color 0.3s ease;
  margin-top: 20px;
}

.checkout-button:hover {
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