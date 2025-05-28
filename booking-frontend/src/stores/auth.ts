import { defineStore } from 'pinia'
import { getInfoApi } from '@/services/user'
import { useRouter } from 'vue-router'
export const useAuthStore = defineStore({
  id: 'auth',
  state: () => ({
    // initialize state from local storage to enable user to stay logged in
    user: null as any | null,
    returnUrl: '',
    isLoggedIn: false,
    token: {
      access: '',
      refresh: '',
    },
  }),
  actions: {
    logout() {
      localStorage.removeItem('refresh_token')
      localStorage.removeItem('access_token')
      this.user = null
      this.isLoggedIn = false
      this.token = {
        access: '',
        refresh: '',
      }
      location.reload()
      // router.push({ name: "login" });

    },
    setUser(user: any) {
      this.user = user
    },
 
    async setupAuth() {
      try {
        console.log('Bắt đầu gọi setupAuth') // Log khi bắt đầu thực thi

        const access_token = localStorage.getItem('access_token') as string
        console.log('Access token từ localStorage:', access_token) // Log token từ localStorage

        if (access_token) {
          this.token.access = access_token
          console.log('Token đã được gán:', this.token.access)

          const user = await getInfoApi()
          console.log('Dữ liệu người dùng trả về từ API:', user)

          if (user) {
            this.user = user
            this.isLoggedIn = true
           
          }
        } else {
          console.log('Không tìm thấy token trong localStorage')
        }
      } catch (error) {
        console.error('Lỗi khi setupAuth:', error)
      }
    },
    setToken(access: string) {
      if (access) {
        this.token.access = access
        localStorage.setItem('access_token', access)
      }
    },
  },
  getters: {
    getUser: (state) => state.user,
    getIsLoggedIn: (state) => state.isLoggedIn,
  },
})
// import { defineStore } from 'pinia'
// import { getInfoApi } from '@/services/user'

// export const useAuthStore = defineStore({
//   id: 'auth',
//   state: () => ({
//     user: null as any | null,
//     returnUrl: '',
//     isLoggedIn: false,
//     token: '', // chỉ còn 1 token duy nhất
//   }),
//   actions: {
//     logout() {
//       localStorage.removeItem('token')
//       this.user = null
//       this.isLoggedIn = false
//       this.token = ''
//       location.reload()
//     },
//     setUser(user: any) {
//       this.user = user
//     },
//     async setupAuth() {
//       try {
//         const token = localStorage.getItem('token') as string

//         if (token) {
//           this.token = token
//           const response = await getInfoApi()
//           console.log('LOG user', response)

//           if (response && response.data) {
//             this.user = response.data
//             this.isLoggedIn = true
//             console.log('Login state:', this.isLoggedIn, this.user)
//           }
//         }
//       } catch (error) {
//         console.log('Setup auth error', error)
//       }
//     },
//     setToken(token: string) {
//       if (token) {
//         this.token = token
//         localStorage.setItem('token', token)
//       }
//     },
//   },
//   getters: {
//     getUser: (state) => state.user,
//     getIsLoggedIn: (state) => state.isLoggedIn,
//   },
// })
