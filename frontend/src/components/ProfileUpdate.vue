<script>
import { ref } from 'vue';

export default {
  setup() {
    const searchType = ref('product')
    const searchQuery = ref('')
    const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
    const userInfo = ref({
      username: 'johndoe',
      loginId: 'johndoe123',
      nickname: 'JohnD',
      address: '서울특별시 강남구 테헤란로 123',
      phoneNumber: '010-1234-5678',
      info: '안녕하세요. 저는 Hot Item Collector의 열렬한 팬입니다!',
      profileImage: 'https://example.com/profile-image.jpg'
    })

    const showProfileImageModal = ref(false)
    const tempProfileImage = ref(null)

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

    const openProfileImageModal = () => {
      showProfileImageModal.value = true
    }

    const closeProfileImageModal = () => {
      showProfileImageModal.value = false
      tempProfileImage.value = null
    }

    const onFileChange = (event) => {
      const file = event.target.files[0]
      if (file) {
        const reader = new FileReader()
        reader.onload = (e) => {
          tempProfileImage.value = e.target.result
        }
        reader.readAsDataURL(file)
      }
    }

    const updateProfileImage = () => {
      if (tempProfileImage.value) {
        userInfo.value.profileImage = tempProfileImage.value
        closeProfileImageModal()
      }
    }

    const submitForm = () => {
      alert('개인정보가 성공적으로 수정되었습니다.')
      // 여기에서 서버로 데이터를 전송하는 로직을 구현합니다.
    }

    return {
      searchType,
      searchQuery,
      categories,
      userInfo,
      showProfileImageModal,
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
      openProfileImageModal,
      closeProfileImageModal,
      onFileChange,
      updateProfileImage,
      submitForm
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

    <main class="container edit-profile">
      <h1>개인정보 수정</h1>
      <form class="edit-profile-form" @submit.prevent="submitForm">
        <div class="image-upload">
          <img :src="userInfo.profileImage" alt="프로필 이미지" class="profile-image">
          <button type="button" class="profile-image-change-btn" @click="openProfileImageModal">프로필 사진 변경</button>
        </div>
        <div class="form-group">
          <label for="username">사용자 이름</label>
          <input type="text" id="username" v-model="userInfo.username" readonly>
        </div>
        <div class="form-group">
          <label for="loginId">로그인 ID</label>
          <input type="text" id="loginId" v-model="userInfo.loginId" readonly>
        </div>
        <div class="form-group">
          <label for="nickname">닉네임</label>
          <input type="text" id="nickname" v-model="userInfo.nickname">
        </div>
        <div class="form-group">
          <label for="address">주소</label>
          <input type="text" id="address" v-model="userInfo.address">
        </div>
        <div class="form-group">
          <label for="phoneNumber">전화번호</label>
          <input type="tel" id="phoneNumber" v-model="userInfo.phoneNumber">
        </div>
        <div class="form-group">
          <label for="info">자기소개</label>
          <textarea id="info" v-model="userInfo.info"></textarea>
        </div>
        <button type="submit" class="submit-btn">수정 완료</button>
      </form>
    </main>

    <!-- 프로필 이미지 변경 모달 -->
    <div class="modal" v-if="showProfileImageModal">
      <div class="modal-content">
        <h2>프로필 사진 변경</h2>
        <input type="file" @change="onFileChange" accept="image/*">
        <div class="modal-buttons">
          <button @click="closeProfileImageModal">취소</button>
          <button @click="updateProfileImage">변경</button>
        </div>
      </div>
    </div>

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
  --light-gray: #f0f0f0;
  --border-color: #ddd;
  --modal-bg: rgba(0, 0, 0, 0.5);
  --category-color: #f1f1f1;
  --category-hover-color: #e0e0e0;
  --button-hover-color: #FFCCCB;
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
  background-color: var(--category-color);
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
  background-color: var(--category-hover-color);
  color: var(--text-color);
}

/* Edit Profile Styles */
.edit-profile {
  max-width: 600px;
  margin: 40px auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

.edit-profile h1 {
  color: var(--text-color);
  margin-bottom: 20px;
  font-size: 28px;
  font-weight: bold;
  text-align: center;
}

.edit-profile-form {
  display: grid;
  gap: 15px;
}

.form-group {
  display: flex;
  flex-direction: column;
}

.form-group label {
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input,
.form-group textarea {
  padding: 10px;
  border: 1px solid var(--border-color);
  border-radius: 5px;
  font-size: 16px;
}

.form-group input[readonly] {
  background-color: var(--light-gray);
}

.form-group textarea {
  resize: vertical;
  min-height: 100px;
}

.submit-btn,
.profile-image-change-btn {
  padding: 10px 20px;
  background-color: var(--category-color);
  color: var(--text-color);
  border: 1px solid var(--border-color);
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  font-weight: bold;
}

.submit-btn:hover,
.profile-image-change-btn:hover {
  background-color: var(--button-hover-color);
}

.profile-image {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  object-fit: cover;
  margin-bottom: 15px;
}

.image-upload {
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* Modal Styles */
.modal {
  display: none;
  position: fixed;
  z-index: 1000;
  left: 0;
  top: 0;
  width: 100%;
  height: 100%;
  background-color: var(--modal-bg);
}

.modal-content {
  background-color: var(--bg-color);
  margin: 15% auto;
  padding: 20px;
  border-radius: 10px;
  width: 300px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.modal h2 {
  margin-top: 0;
}

.modal input {
  width: 100%;
  padding: 10px;
  margin: 10px 0;
  border: 1px solid var(--border-color);
  border-radius: 5px;
}

.modal-buttons {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.modal-buttons button {
  margin-left: 10px;
  padding: 10px 20px;
  background-color: var(--category-color);
  color: var(--text-color);
  border: 1px solid var(--border-color);
  border-radius: 5px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  font-weight: bold;
}

.modal-buttons button:hover {
  background-color: var(--button-hover-color);
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