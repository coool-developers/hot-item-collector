<script>
import {ref, computed, onMounted} from 'vue';
import Header from './AppHeader.vue';
import AppFooter from './AppFooter.vue';
import {useRoute} from "vue-router";
import axios from "axios";
import Cookies from "js-cookie";

export default {
  components: { Header, AppFooter },
  setup() {
    const isLoggedIn = ref(true)
    const route = useRoute();
    const userId = route.params.userId;
    const accessToken = Cookies.get('access_token')
    const isFollowing = ref(false)

    const user = ref({
      id: '',
      name: '',
      profileImage: '',
      bio: '',
      followers: 0
    });

    const fetchUser = async () => {
      if(userId){
        const response = await axios.get(`http://localhost:8080/users/profile/${userId}`);
        console.log(response.data.result);

        user.value = response.data.result;
      }else {
        console.error('User Id is missing in route parameters');
      }
    }

    onMounted(fetchUser);
    const products = ref([])

    const fetchProduct = async () => {
      if(userId){
        const response = await axios.get(`http://localhost:8080/products/sale/${userId}`,{
          params: {
            page: 1,
            size: 4
          }
        });
        console.log(response.data.result);

        products.value = response.data.result.map(product =>({
          id: product.id,
          name: product.name,
          image: product.image.imageUrl,
          status:product.status
        }));
      }else {
        console.error('User Id is missing in route parameters');
      }
    }
    onMounted(fetchProduct);

    const fetchFollowStatus = async () => {
      try {
        await fetchProduct()
        const response = await axios.get(`http://localhost:8080/follow/${userId}`, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = response.data.result.userFollow
        console.log("팔로우 여부 불러오기 완료")
      } catch(error) {
        console.error(error)
      }
    }

    onMounted(() => {
      fetchProduct();
      fetchFollowStatus();
    });


    const follow = async () => {
      try {
        await axios.post(`http://localhost:8080/follow/${userId}`, {}, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = true;
        console.log('팔로우 성공')
      } catch (error) {
        console.error('팔로우 실패:', error);
      }
    };

    const unfollow = async () => {
      try {
        await axios.delete(`http://localhost:8080/follow/${userId}`, {
          headers: {
            'Authorization': accessToken
          }
        });
        isFollowing.value = false;
        console.log('팔로우 취소 성공');
      } catch (error) {
        console.error('팔로우 취소 실패:', error);
      }
    };

    const itemsPerPage = 12
    const currentPage = ref(1)

    const totalPages = computed(() => Math.ceil(products.value.length / itemsPerPage))

    const displayedProducts = computed(() => {
      const start = (currentPage.value - 1) * itemsPerPage
      const end = start + itemsPerPage
      return products.value.slice(start, end)
    })

    const prevPage = () => {
      if (currentPage.value > 1) {
        currentPage.value--
      }
    }

    const nextPage = () => {
      if (currentPage.value < totalPages.value) {
        currentPage.value++
      }
    }

    const toggleFollow = async () => {
      if (isFollowing.value) {
        await unfollow();
      } else {
        await follow();
      }
    }


    return {
      isLoggedIn,
      isFollowing,
      user,
      displayedProducts,
      currentPage,
      totalPages,
      prevPage,
      nextPage,
      toggleFollow
    }
  }
}
</script>

<template>
  <div id="app">
    <Header/>
    <main class="container my-info">
      <section class="profile-section">
        <img :src="user.profileImage.imageUrl" alt="프로필 이미지" class="profile-image">
        <div class="profile-details">
          <h1 class="profile-name">{{ user.nickname }}</h1>
          <p class="profile-bio">{{ user.info }}</p>
          <div class="profile-stats">
            <span>팔로워: {{ user.followerCount }}</span>
            <button v-if="isLoggedIn" class="follow-button" @click="toggleFollow">
              {{ isFollowing ? '팔로우 취소' : '팔로우' }}
            </button>
          </div>
        </div>
      </section>

      <section class="product-section">
        <h2>판매 상품</h2>
        <div class="product-list">
          <div v-for="product in displayedProducts" :key="product.id" class="product-card"
               @click="goToProduct(product.id)">
            <img :src="product.image" :alt="product.name" class="product-image">
            <div class="product-info">
              <div class="product-name">{{ product.name }}</div>
              <div class="product-id">ID: {{ product.id }}</div>
            </div>
          </div>
        </div>
        <div class="pagination">
          <button @click="prevPage" :disabled="currentPage === 1">이전</button>
          <span>{{ currentPage }} / {{ totalPages }}</span>
          <button @click="nextPage" :disabled="currentPage === totalPages">다음</button>
        </div>
      </section>
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
  --kakao-color: #FEE500;
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
  width: 200px;
  height: 200px;
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
  color: var(--text-color);
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
  background-color: var(--bg-color);
  color: var(--text-color);
  border: 1px solid #e0e0e0;
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
}

.profile-stats button:hover {
  background-color: var(--hover-color);
  color: var(--bg-color);
}

.product-section {
  margin-bottom: 30px;
}

.product-section h2 {
  font-size: 24px;
  margin-bottom: 20px;
  color: var(--text-color);
}

.product-list {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 20px;
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

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}

.pagination button {
  padding: 10px 20px;
  margin: 0 5px;
  background-color: var(--main-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.pagination button:hover {
  background-color: var(--hover-color);
}

.pagination button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.follow-button {
  padding: 5px 15px;
  background-color: var(--main-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.follow-button:hover {
  background-color: var(--hover-color);
}

</style>