<script>
import {ref, computed, onMounted} from 'vue';
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  setup() {
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const cartItems = ref([]); // 장바구니 항목을 저장할 상태

    const loadCartItems = () => {
      axios.get('http://localhost:8080/cart', {
        headers: {
          'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNzIxOTkxMTUyLCJleHAiOjE5MDE5OTExNTJ9.TTHc4NohF1RfgUQurvHc32VsZWUq1FF6kLK2wvxi-Do'
        }
      }).then(response => {
        cartItems.value = response.data.result; // 응답 데이터를 상태에 저장
      }).catch(error => {
        console.error(error); // 에러 처리
      });
    };

    const totalPrice = computed(() => {
      return cartItems.value.reduce((total, item) => {
        return item.selected ? total + item.price : total
      }, 0)
    })

    const search = () => {
      alert(`검색 유형: ${searchType.value}, 검색어: ${searchQuery.value}`)
    }

    const selectCategory = (category) => {
      alert(`선택한 카테고리: ${category}`)
    }

    const goToProductRegistration = () => {
      alert('물품 등록 페이지로 이동합니다.')
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
      alert('현재 장바구니 페이지입니다.')
    }

    const router = useRouter();

    const buyItem = (item) => {
      const orderData = [item]
      sessionStorage.setItem('orderData', JSON.stringify(orderData));
      router.push({name: 'OrderPage'})
    }

    const removeItem = (item) => {
      const userConfirmed = confirm(`${item.productName}을(를) 장바구니에서 삭제하시겠습니까?`)
      if (!userConfirmed) {
        return;
      }
      axios.delete(`http://localhost:8080/cart/${item.productId}`, {
        headers: {
          'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyMiIsImF1dGgiOiJVU0VSIiwiaWF0IjoxNzIxOTkxMTUyLCJleHAiOjE5MDE5OTExNTJ9.TTHc4NohF1RfgUQurvHc32VsZWUq1FF6kLK2wvxi-Do'
        }
      }).then(response => {
        if (response.status == 200) {
          window.location.reload();
        }
      }).catch(error => {
        console.error(error);
      })
    }

    const orderItems = () => {
      const orderData = cartItems.value.filter(item => item.selected)
      if (orderData.length > 0) {

        sessionStorage.setItem('orderData', JSON.stringify(orderData));
        router.push({name: 'OrderPage'})
      } else {
        alert('선택한 상품이 없습니다.')
      }
    }

    onMounted(() => {
      loadCartItems(); // 컴포넌트가 마운트될 때 API 호출
    });

    return {
      searchType,
      searchQuery,
      categories,
      cartItems,
      totalPrice,
      search,
      selectCategory,
      goToProductRegistration,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      buyItem,
      removeItem,
      orderItems
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
          <button @click="goToProductRegistration">물품등록</button>
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
          <a v-for="category in categories" :key="category"
             @click.prevent="selectCategory(category)" href="#" class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <main class="container cart">
      <h1>장바구니</h1>
      <div class="cart-items">
        <div v-for="item in cartItems" :key="item.id" class="cart-item">
          <div class="cart-item-left">
            <input type="checkbox" v-model="item.selected" class="cart-item-checkbox">
            <img :src="item.productImage.imageUrl" :alt="item.productImage.filename" class="cart-item-image">
            <div class="cart-item-details">
              <div class="cart-item-name">{{ item.productName }}</div>
              <div class="cart-item-price">{{ item.price }}원</div>
              <div class="cart-item-seller">판매자: {{ item.seller }}</div>
            </div>
          </div>
          <div class="cart-item-actions">
            <button @click="buyItem(item)">구매하기</button>
            <button @click="removeItem(item)">삭제</button>
          </div>
        </div>
      </div>
      <div class="cart-summary">
        <div class="cart-total">총 금액: {{ totalPrice }}원</div>
        <button class="cart-order-button" @click="orderItems">주문하기</button>
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

/* Cart Styles */
.cart {
  padding: 20px 0;
}

.cart-items {
  margin-bottom: 20px;
}

.cart-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
}

.cart-item-left {
  display: flex;
  align-items: center;
}

.cart-item-checkbox {
  margin-right: 10px;
}

.cart-item-image {
  width: 100px;
  height: 100px;
  object-fit: cover;
  margin-right: 20px;
}

.cart-item-details {
  flex-grow: 1;
}

.cart-item-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.cart-item-price {
  color: var(--main-color);
  font-weight: bold;
  margin-bottom: 5px;
}

.cart-item-seller {
  font-size: 14px;
  color: #666;
  margin-bottom: 10px;
}

.cart-item-actions {
  display: flex;
  gap: 10px;
}

.cart-item-actions button {
  padding: 5px 10px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 3px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.cart-item-actions button:hover {
  background-color: var(--hover-color);
}

.cart-summary {
  background-color: var(--bg-color);
  padding: 20px;
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.cart-total {
  font-size: 18px;
  font-weight: bold;
}

.cart-order-button {
  padding: 10px 20px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.cart-order-button:hover {
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