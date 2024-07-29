<style scoped>
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

</style>

<template>
  <div id="app">
    <Header />
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
              <div class="product-name">상품명: {{ product.name }}</div>
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
    <AppFooter />
  </div>
</template>

<script>
import { ref, onMounted  } from 'vue'
import Header from './AppHeader.vue';
import AppFooter from './AppFooter.vue';
import axios from 'axios';
import Cookies from "js-cookie";
import { useRouter } from 'vue-router';

export default {
  name: 'App',
  components: {Header, AppFooter},
  setup() {
    const router = useRouter(); // For navigation
    const user = ref({
      id: '',
      name: '',
      profileImage: '',
      bio: '',
      followers: 0
    });

    const registeredProducts = ref([]);
    const purchasedProducts = ref([]);
    const likedProducts = ref([]);


    const fetchData = async () => {
      const accessToken = Cookies.get('access_token');
      try {
        // Fetch user profile data
        const userResponse = await axios.get('http://localhost:8080/users/profile', {
          headers: {
            'Authorization': accessToken
          }
        });
        // Check if bio is missing
        if (!userResponse.data.result.info) {
          router.push('/profile/update');
          return; // Exit the function if redirecting
        }
        user.value = {
          id: userResponse.data.result.id,
          name: userResponse.data.result.nickname,
          profileImage: userResponse.data.result.profileImage.imageUrl,
          bio: userResponse.data.result.info,
          followers: userResponse.data.result.followers || 0
        };

        // Fetch registered products
        const registeredResponse = await axios.get('http://localhost:8080/products/sale', {
          params: {
            page: 1,
            size: 4,
            status:'ON_SALE'
          },
          headers: {
            'Authorization': accessToken
          }
        });
        console.log(registeredResponse.data);

        registeredProducts.value = registeredResponse.data.result.map(product => ({
          id: product.id,
          name: product.name,
          image: product.image.imageUrl
        }));


      } catch (error) {
        console.error('Failed to fetch data:', error);
      }
    };

    onMounted(fetchData);



    const showFollowers = () => {
      alert('팔로워 목록 표시 기능은 아직 구현되지 않았습니다.')
    }

    const goToProductRegistration = () => {
      alert('상품 등록 페이지로 이동합니다.')
    }

    const goToProduct = (productId) => {
      alert(`상품 ID ${productId}의 상세 페이지로 이동합니다.`)
      router.push(`/product/update/${productId}`);
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
      user,
      registeredProducts,
      purchasedProducts,
      likedProducts,
      showFollowers,
      goToProductRegistration,
      goToProduct,
      goToRegisteredProducts,
      goToPurchasedProducts,
      goToLikedProducts,
    }
  }
}
</script>
