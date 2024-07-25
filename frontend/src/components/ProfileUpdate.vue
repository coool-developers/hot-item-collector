<template>
  <div>
    <!-- Header Section -->
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

    <!-- Categories Section -->
    <nav class="categories">
      <div class="container">
        <div class="categories-container">
          <a v-for="category in categories" :key="category" @click.prevent="selectCategory(category)" href="#"
             class="category-item">
            {{ category }}
          </a>
        </div>
      </div>
    </nav>

    <!-- Main Content Section -->
    <main class="container edit-profile">
      <h1>개인정보 수정</h1>
      <div class="edit-profile-content">
        <h2>비밀번호 확인</h2>
        <p>회원님의 정보를 안전하게 보호하기 위해 비밀번호를 다시 한번 확인합니다.</p>
        <form class="password-form" @submit.prevent="confirmPassword">
          <label for="password">비밀번호:</label>
          <input type="password" id="password" v-model="password" placeholder="비밀번호를 입력하세요" required>
          <button type="submit">확인</button>
        </form>
        <p class="privacy-notice">
          회원님의 개인정보를 신중히 취급하며, 회원님의 동의 없이는<br>
          기재하신 회원정보를 공개 및 변경하지 않습니다.
        </p>
      </div>
    </main>

    <!-- Footer Section -->
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

<script setup>
import { ref } from 'vue'

const searchType = ref('product')
const searchQuery = ref('')
const categories = ref(['식품', '뷰티', '패션&주얼리', '공예품', '홈리빙', '반려동물'])
const password = ref('')

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
  alert('장바구니 페이지로 이동합니다.')
}

const confirmPassword = () => {
  // 여기에서 비밀번호 확인 로직을 구현합니다.
  // 실제 애플리케이션에서는 서버로 비밀번호를 전송하여 확인해야 합니다.
  alert('비밀번호가 확인되었습니다. 개인정보 수정 페이지로 이동합니다.')
  // 비밀번호가 확인되면 개인정보 수정 페이지로 이동하는 로직을 추가합니다.
}
</script>

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

/* Edit Profile Styles */
.edit-profile {
  max-width: 600px;
  margin: 40px auto;
  text-align: left;
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
}

.edit-profile-content {
  border: 2px solid var(--border-color);
  border-radius: 8px;
  padding: 20px;
  background-color: var(--bg-color);
}

.edit-profile h2 {
  color: var(--text-color);
  margin-bottom: 15px;
  font-size: 20px;
  font-weight: bold;
  text-align: center;
}

.edit-profile p {
  margin-bottom: 20px;
  line-height: 1.6;
  font-size: 14px;
  text-align: center;
}

.password-form {
  display: flex;
  flex-direction: row;
  align-items: center;
  justify-content: center;
  margin-bottom: 20px;
}

.password-form label {
  margin-right: 10px;
  font-size: 14px;
}

.password-form input {
  width: 200px;
  padding: 10px;
  margin-right: 10px;
  border: 1px solid #ddd;
  border-radius: 5px;
  font-size: 14px;
}

.password-form button {
  padding: 10px 20px;
  background-color: var(--button-color);
  color: var(--bg-color);
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  font-size: 14px;
}

.password-form button:hover {
  background-color: var(--hover-color);
}

.privacy-notice {
  font-size: 14px;
  color: #666;
  padding: 15px;
  background-color: var(--light-gray);
  border-radius: 5px;
  text-align: center;
  margin-top: 20px;
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
