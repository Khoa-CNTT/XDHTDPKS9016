// plugins/toast.ts
import Vue3Toastify, { type ToastContainerOptions } from 'vue3-toastify'
import 'vue3-toastify/dist/index.css'

export default {
  Vue3Toastify,
  options: {
    autoClose: 10000,
    transition: 'slide',
    hideProgressBar: true,
    position: 'bottom-left',
  } as ToastContainerOptions,
}
