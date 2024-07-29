<script>
import Header from './AppHeader.vue';
import { ref, computed, onMounted } from 'vue';
import axios from "axios";
import { useRouter, useRoute } from "vue-router";
import AppFooter from './AppFooter.vue';
import Cookies from 'js-cookie';
export default {
  components: { Header, AppFooter },
  setup() {
    const isLoggedIn = ref(false);
    const currentPage = ref(1);
    const totalPages = ref(1);
    const itemsPerPage = 16;
    const products = ref([]); // 초기값을 빈 배열로 설정
    const searchQuery = ref('');
    const categoryType = ref('');

    const categories = {
      '식품': 'FOOD',
      '뷰티': 'BEAUTY',
      '패션&주얼리': 'FASION',
      '공예품': 'CRAFTS',
      '홈리빙': 'HOME_LIVING',
      '반려동물': 'PET'
    };

    const router = useRouter();
    const route = useRoute();

    const pageTitle = computed(() => `${categoryType.value} 상품 목록`);

    const displayedItems = computed(() => {
      if (!Array.isArray(products.value)) {
        return []; // products가 배열이 아닌 경우 빈 배열 반환
      }
      const start = (currentPage.value - 1) * itemsPerPage;
      const end = start + itemsPerPage;
      return products.value.slice(start, end);
    });


    const fetchProducts = async (page) => {
      try {
        const url = `/products/search?page=${page}&size=${itemsPerPage}&category=${searchQuery.value}`;
        const accessToken = Cookies.get('access_token');
        const response = await axios.get(url,{
          headers: {
            'Authorization': accessToken
          }
        });
        console.log(response.data);
        const data = response.data.result;
        products.value = data || [];
        totalPages.value = Math.ceil((data.total || 0) / itemsPerPage);
      } catch (error) {
        console.error('Failed to fetch products:', error);
      }
    };

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--;
        fetchProducts(currentPage.value);
      }
    };

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++;
        fetchProducts(currentPage.value);
      }
    };

    const selectCategory = (category) => {
      const englishCategory = categories[category] || category;
      console.log(englishCategory);
      searchQuery.value = englishCategory;
      currentPage.value = 1;
      fetchProducts(currentPage.value);
    };

    const goToSellerPage = (userId) => {
      router.push(`/seller/${userId}`);
    };

    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`);
      router.push(`/product/detail/${productId}`);
    };

    onMounted(() => {
      const initialCategory = route.query.searchQuery;
      categoryType.value = initialCategory;
      searchQuery.value = categories[initialCategory] || initialCategory;
      fetchProducts(currentPage.value);
    });

    return {
      isLoggedIn,
      searchQuery,
      currentPage,
      totalPages,
      itemsPerPage,
      products,
      displayedItems,
      pageTitle,
      prevPage,
      nextPage,
      goToProduct,
      goToSellerPage,
      selectCategory
    };
  }
};
</script>

<template>
  <div id="app">
    <Header/>
    <main class="container">
      <section class="search-results">
        <h2>{{ pageTitle }}</h2>
        <div class="item-grid">
          <div v-for="item in displayedItems" :key="item.id" class="item-card"
               @click="goToProduct(item.id)">
            <img :src="item.image.imageUrl" :alt="item.name">
            <div class="item-info">
              <div class="item-name">{{ item.name }}</div>
              <div class="item-seller">
                판매자: <a @click.stop="goToSellerPage(item.userId)" :href="'/seller/' + item.userId">{{ item.userName }}</a>
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
    <AppFooter />
  </div>
</template>

<style scoped>
/* 기본 스타일 추가 */
#app {
  font-family: Arial, sans-serif;
}

.container {
  width: 80%;
  margin: 0 auto;
}

.item-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4개의 열을 만들고, 각 열이 동일한 비율로 공간을 차지하도록 설정 */
  gap: 16px; /* 아이템 간의 간격을 설정 */
}

.item-card {
  background: #fff;
  border: 1px solid #ddd;
  border-radius: 8px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.2s;
}

.item-card:hover {
  transform: scale(1.05);
}

.item-card img {
  width: 100%;
  height: auto;
}

.item-info {
  padding: 10px;
}

.item-name {
  font-weight: bold;
}

.item-seller {
  font-size: 0.9em;
  color: #555;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  gap: 10px;
}

button {
  padding: 10px 20px;
  border: none;
  border-radius: 4px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}

button:disabled {
  background-color: #ddd;
}



label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 10px;
  border: 1px solid #ddd;
  border-radius: 4px;
}


</style>
