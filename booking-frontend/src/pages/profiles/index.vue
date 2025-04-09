<template>
  <div class="container">
    <div class="profile-main">
      <div class="profile-header">
        <div class="avatar">
          <span>NT</span>
        </div>
        <h1>Hồ sơ người dùng</h1>
      </div>

      <div class="profile-content">
        <div class="name">
          <div class="info-label">
            <h3>Họ tên</h3>
          </div>
          <div class="info-content">
            <span class="sp1">Nguyễn Phước T</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>

        <div class="name">
          <div class="info-label">
            <h3>Tuổi</h3>
          </div>
          <div class="info-content">
            <span class="sp1">21</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>

        <div class="name">
          <div class="info-label">
            <h3>Giới tính</h3>
          </div>
          <div class="info-content">
            <span class="sp1">Nam</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>

        <div class="name">
          <div class="info-label">
            <h3>Ngày sinh</h3>
          </div>
          <div class="info-content">
            <span class="sp1">19/06/2003</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>

        <div class="name">
          <div class="info-label">
            <h3>Email</h3>
          </div>
          <div class="info-content">
            <span class="sp1">nguyenphuocth@gmail.com</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>

        <div class="name">
          <div class="info-label">
            <h3>Địa chỉ</h3>
          </div>
          <div class="info-content">
            <span class="sp1">Nong Son Quang Nam</span>
            <span class="sp2">
              <i class="edit-icon"></i>
              Chỉnh sửa
            </span>
          </div>
        </div>
      </div>

      <div class="btn-container">
        <button class="bt-save">Lưu thay đổi</button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  name: 'UserProfile',
}
</script>
<style scoped>
.container {
  padding: 20px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background-color: #e0e3e9;
  min-height: 100vh;
}

.profile-main {
  width: 800px;
  background-color: #ffffff;
  border-radius: 12px;
  /* box-shadow: 0 8px 20px rgba(0, 0, 0, 0.05); */
  padding: 30px;
  margin: 40px auto;
  transition: all 0.3s ease;
}

.profile-header {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-bottom: 40px;
  position: relative;
}

.avatar {
  width: 100px;
  height: 100px;
  background: linear-gradient(135deg, #6366f1, #4f46e5);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 20px;
  box-shadow: 0 8px 16px rgba(99, 102, 241, 0.3);
}

.avatar span {
  color: white;
  font-size: 32px;
  font-weight: bold;
}

h1 {
  text-align: center;
  font-weight: 600;
  text-transform: uppercase;
  font-size: 24px;
  color: #1e293b;
  margin-bottom: 10px;
  letter-spacing: 1px;
}

.profile-content {
  margin-bottom: 30px;
}

.name {
  margin-bottom: 15px;
  background-color: #ffffff;
  border-radius: 10px;
  padding: 0;
  border: 1px solid #d7dbdf;
  overflow: hidden;
}

.name:hover {
  transform: translateY(-2px);
}

.info-label {
  padding: 12px 20px;
  background-color: #e9ecef;
  border-bottom: 1px solid #e9eef6;
}

.name h3 {
  font-size: 14px;
  color: #64748b;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.info-content {
  padding: 15px 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.sp1 {
  font-size: 16px;
  font-weight: 500;
  color: #334155;
}

.sp2 {
  color: #6366f1;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
}

.sp2:hover {
  color: #4f46e5;
}

.edit-icon {
  display: inline-block;
  width: 16px;
  height: 16px;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%236366f1' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M11 4H4a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2v-7'%3E%3C/path%3E%3Cpath d='M18.5 2.5a2.121 2.121 0 0 1 3 3L12 15l-4 1 1-4 9.5-9.5z'%3E%3C/path%3E%3C/svg%3E");
  background-size: contain;
  margin-right: 6px;
}

.btn-container {
  text-align: center;
  margin-top: 30px;
}

.bt-save {
  display: inline-block;
  padding: 12px 40px;
  border: none;
  border-radius: 8px;
  background: linear-gradient(135deg, #6366f1, #4f46e5);
  color: white;
  font-weight: 600;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(99, 102, 241, 0.3);
  font-size: 16px;
}

.bt-save:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(99, 102, 241, 0.3);
}
</style>