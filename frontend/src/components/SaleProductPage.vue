<script>
import { ref,computed } from 'vue';
import AppHeader from "@/components/AppHeader.vue";
import AppFooter from "@/components/AppFooter.vue";
import Cookies from "js-cookie";
import axios from "axios";
import {useRouter} from "vue-router";

export default {
  components: {AppFooter, AppHeader},
  setup() {
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const filter = ref('all')
    const currentPage = ref(1)
    const itemsPerPage = 8
    const products = ref()
    const totalPages = ref(1)
    const router = useRouter();

    const fetchProducts = async (filterType) => {
      const accessToken = Cookies.get('access_token');
      let status;

      switch (filterType) {
        case 'selling':
          status = 'ON_SALE';
          break;
        case 'sold':
          status = 'SOLD_OUT';
          break;
        default:
          status = null;
      }

      try {
        const params = status ? { status } : {};
        const response = await axios.get('http://localhost:8080/products/sale', {
          headers: {
            'Authorization': accessToken
          },
          params
        });
        const data = response.data;
        products.value = data.result;
        totalPages.value = Math.ceil(data.total / itemsPerPage);
      } catch (error) {
        console.error('Error fetching products:', error);
      }
    };

    const displayedProducts = computed(() => {
      return products.value
    })

    const setFilter = (FilterType) => {
      filter.value = FilterType
      currentPage.value = 1
      fetchProducts(FilterType);
    }

    const prevPage = async () => {
      if (currentPage.value > 1) {
        currentPage.value--
        await fetchProducts()
      }
    }

    const nextPage = async () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++
        await fetchProducts()
      }
    }

    const search = () => {
      alert(`검색 유형: ${searchType.value}, 검색어: ${searchQuery.value}`)
    }

    const selectCategory = (category) => {
      alert(`선택한 카테고리: ${category}`)
    }

    const goToProductRegistration = () => {
      alert('상품 등록 페이지로 이동합니다.')
      router.push(`/product/upload`)
    }

    const goToProductManagement = () => {
      alert('상품 관리 페이지로 이동합니다.')
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
    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
      router.push(`/product/update/${productId}`);
    }

    // Initial fetch
    fetchProducts()

    return {
      searchType,
      searchQuery,
      categories,
      filter,
      currentPage,
      totalPages,
      displayedProducts,
      search,
      selectCategory,
      setFilter,
      prevPage,
      nextPage,
      goToProductRegistration,
      goToProductManagement,
      goToOrderManagement,
      viewMyInfo,
      editProfile,
      logout,
      deleteAccount,
      goToCart,
      goToProduct
    }
  }
}
</script>

<template>
  <div id="app">
   <AppHeader />

    <main class="container product-list">
      <div class="product-list-header">
        <h1 class="product-list-title">판매 상품 목록</h1>
        <div class="product-list-actions">
          <button @click="goToProductManagement">상품 관리</button>
          <button @click="goToProductRegistration">상품 등록</button>
        </div>
      </div>
      <div class="product-status-filter">
        <button :class="{ active: filter === 'all' }" @click="setFilter('all')">전체</button>
        <button :class="{ active: filter === 'selling' }" @click="setFilter('selling')">판매중</button>
        <button :class="{ active: filter === 'sold' }" @click="setFilter('sold')">판매완료</button>
      </div>
      <div class="product-grid">
        <div v-for="product in displayedProducts" :key="product.id" class="product-card" @click="goToProduct(product.id)">
          <img :src="product.image.imageUrl" :alt="product.name" class="product-image">
          <div class="product-info">
            <div class="product-id">ID: {{ product.id }}</div>
            <div class="product-name">{{ product.name }}</div>
            <span :class="['product-status', product.status === 'SOLD_OUT' ? 'status-sold' : 'status-selling']">
              {{ product.status === 'SOLD_OUT' ? '판매완료' : '판매중' }}
            </span>
          </div>
        </div>
      </div>
      <div class="pagination">
        <button @click="prevPage" :disabled="currentPage === 1">&lt; 이전</button>
        <span>{{ currentPage }} / {{ totalPages }}</span>
        <button @click="nextPage" :disabled="currentPage === totalPages">다음 &gt;</button>
      </div>
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
  --card-border: #e0e0e0;
  --status-sold: #4CAF50;
  --status-selling: #2196F3;
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

/* Product List Styles */
.product-list {
  padding: 20px 0;
}

.product-list-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.product-list-title {
  font-size: 24px;
  font-weight: bold;
}

.product-list-actions button {
  padding: 10px 20px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 16px;
  margin-left: 10px;
  transition: background-color 0.3s ease;
}

.product-list-actions button:hover {
  background-color: var(--hover-color);
}

.product-status-filter {
  margin-bottom: 20px;
}

.product-status-filter button {
  padding: 8px 16px;
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid var(--card-border);
  border-radius: 5px;
  cursor: pointer;
  font-size: 14px;
  margin-right: 10px;
  transition: all 0.3s ease;
}

.product-status-filter button.active {
  background-color: var(--button-color);
  color: var(--bg-color);
  border-color: var(--button-color);
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.product-card {
  border: 1px solid var(--card-border);
  border-radius: 5px;
  overflow: hidden;
  transition: box-shadow 0.3s ease;
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
  font-size: 12px;
  color: #666;
  margin-bottom: 5px;
}

.product-status {
  display: inline-block;
  padding: 3px 8px;
  border-radius: 3px;
  font-size: 12px;
  font-weight: bold;
}

.status-selling {
  background-color: var(--status-selling);
  color: var(--bg-color);
}

.status-sold {
  background-color: var(--status-sold);
  color: var(--bg-color);
}

.pagination {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 20px;
}

.pagination button {
  padding: 8px 12px;
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid var(--card-border);
  cursor: pointer;
  font-size: 14px;
  transition: all 0.3s ease;
}

.pagination button:hover:not(:disabled) {
  background-color: var(--hover-color);
  color: var(--bg-color);
}

.pagination button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.pagination span {
  margin: 0 10px;
  font-size: 16px;
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