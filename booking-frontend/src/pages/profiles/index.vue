<template>
  <div class="min-h-screen bg-gray-100 py-10 px-4">
    <div class="max-w-4xl mx-auto bg-white rounded-xl shadow-md p-8">
      <div class="flex flex-col items-center mb-10">
        <div
          class="w-24 h-24 rounded-full bg-gradient-to-br from-indigo-500 to-indigo-700 flex items-center justify-center text-white text-3xl font-bold shadow-lg"
        >
          NT
        </div>
        <h1 class="text-xl font-semibold uppercase text-slate-800 mt-4">Hồ sơ người dùng</h1>
      </div>

      <div class="space-y-6">
        <div
          v-for="(field, key) in profileFields"
          :key="key"
          class="rounded-lg border border-gray-200 hover:-translate-y-0.5 transition-transform"
        >
          <div class="bg-gray-100 px-4 py-2 border-b border-gray-200">
            <h3 class="text-sm font-semibold text-gray-600 uppercase">{{ field.label }}</h3>
          </div>
          <div class="flex items-center justify-between px-4 py-3">
            <template v-if="editingField === key">
              <input
                v-model="editedProfile[key]"
                type="text"
                class="flex-1 mr-2 px-3 py-2 border rounded-md text-sm"
              />
              <span
                @click="saveField(key)"
                class="text-indigo-600 text-sm font-medium cursor-pointer mr-2"
                >💾 Lưu</span
              >
              <span
                @click="cancelEdit"
                class="text-indigo-600 text-sm font-medium cursor-pointer"
                >❌ Hủy</span
              >
            </template>
            <template v-else>
              <span class="text-base font-medium text-slate-700">{{ userProfile[key] }}</span>
              <span
                @click="editField(key)"
                class="text-indigo-600 text-sm font-medium cursor-pointer flex items-center ml-2"
              >
                <svg
                  class="w-4 h-4 mr-1"
                  fill="none"
                  stroke="currentColor"
                  stroke-width="2"
                  viewBox="0 0 24 24"
                  xmlns="http://www.w3.org/2000/svg"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M11 4H4a2 2 0 00-2 2v14a2 2 0 002 2h14a2 2 0 002-2v-7"
                  ></path>
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    d="M18.5 2.5a2.121 2.121 0 013 3L12 15l-4 1 1-4 9.5-9.5z"
                  ></path>
                </svg>
                Chỉnh sửa
              </span>
            </template>
          </div>
        </div>
      </div>

      <div class="text-center mt-10">
        <button
          @click="saveAll"
          class="bg-indigo-600 hover:bg-indigo-700 text-white font-semibold px-6 py-3 rounded-lg shadow-lg transition-transform hover:-translate-y-0.5"
        >
          Lưu thay đổi
        </button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserProfile',
  data() {
    return {
      editingField: null,
      userProfile: {
        fullName: 'Nguyễn Phước T',
        age: '21',
        gender: 'Nam',
        dob: '19/06/2003',
        email: 'nguyenphuocth@gmail.com',
        address: 'Nong Son Quang Nam',
      },
      editedProfile: {},
      profileFields: {
        fullName: { label: 'Họ tên' },
        age: { label: 'Tuổi' },
        gender: { label: 'Giới tính' },
        dob: { label: 'Ngày sinh' },
        email: { label: 'Email' },
        address: { label: 'Địa chỉ' },
      },
    }
  },
  methods: {
    editField(field) {
      this.editingField = field
      this.editedProfile[field] = this.userProfile[field]
    },
    saveField(field) {
      this.userProfile[field] = this.editedProfile[field]
      this.editingField = null
    },
    cancelEdit() {
      this.editingField = null
    },
    saveAll() {
      alert('Lưu toàn bộ thông tin thành công!')
      console.log('Thông tin đã lưu:', this.userProfile)
    },
  },
}
</script>

<style scoped>
</style>