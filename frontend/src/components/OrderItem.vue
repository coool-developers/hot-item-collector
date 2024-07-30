<script>
import {ref, onMounted} from 'vue'
import {DateTime} from 'luxon'
import Cookies from "js-cookie";
import axios from "axios";
import Header from './AppHeader.vue';
import AppFooter from './AppFooter.vue';

export default {
  components: {Header, AppFooter},
  setup() {
    const startDate = ref('')
    const endDate = ref('')
    const orders = ref([])
    const accessToken = Cookies.get('access_token')

    const formatDate = (dateString) => {
      const date = DateTime.fromISO(dateString)
      return date.toFormat('yyyy년 MM월 dd일 (ccc)', {locale: 'ko'})
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
          const today = DateTime.now();
          endDate.value = today.toFormat('yyyy-MM-dd');
          startDate.value = today.minus({months: 3}).toFormat('yyyy-MM-dd');
          searchPurchases();
        }
    );


    return {
      startDate,
      endDate,
      orders,
      formatDate,
      formatPrice,
      searchPurchases,
    };
  }
};

</script>

<template>
  <div id="app">
    <Header/>
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
          <div v-for="item in order.orderItemResponseDtoList" :key="item.productId" class="product-card"
               @click="goToProductDetail(item.productId)">
            <img :src="item.productImage.imageUrl" :alt="item.productName" class="product-image">
            <div class="product-info">
              <div class="product-details">
                <div class="product-name">{{ item.productName }}</div>
                <div>
                  판매자: <a :href="'/seller/' + item.sellerId" class="seller-name" @click.stop>{{
                    item.sellerNickname
                  }}</a>
                </div>
              </div>
              <div class="product-price">{{ formatPrice(item.price) }}원</div>
              <div class="product-status">{{ item.orderStatus }}</div>
            </div>
          </div>
        </div>
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
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
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
</style>