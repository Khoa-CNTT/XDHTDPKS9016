<!-- <script setup lang="ts">
import { forgotPasswordApi } from '@/services/auth'
import ErrorMessage from '@/components/base/ErrorMessage.vue'
import { Input } from '@/components/ui/input'
import { Button } from '@/components/ui/button'
import { useForm } from 'vee-validate'
import * as yup from 'yup'

const errorEmail = ref()

const handleForgot = async () => {
  try {
    if (!email.value) {
      errorEmail.value = 'Email must not be blank'
      return
    }
    await forgotPasswordApi(email.value)
  } catch (error) {
    notify.error(((error as any).data?.error?.message as string) || 'Send forgot password error')
  }
}

const { errors, defineField } = useForm({
  validationSchema: yup.object({
    email: yup.string().email().required('Email is required'),
  }),
})

const [email, emailAttrs] = defineField('email')
</script>
<template>
  <div class="h-full flex p-8">
    <div class="flex-1 flex justify-center items-center">
      <form
        class="form-shadow p-6 rounded-xl"
        @submit="handleForgot"
      >
        <div class="flex items-center gap-0.5 mb-4">
          <h1 class="text-[344054] text-lg font-semibold mt-3">Forgot password</h1>
        </div>
        <div>
          <h2 class="mt-1 text-[#667085]">Enter your email to reset password</h2>
        </div>
        <div class="mt-6">
          <div class="form-data">
            <Label for="email">Email</Label>
            <Input
              id="email"
              v-model="email"
              placeholder="Enter email..."
              v-bind="emailAttrs"
              :invalid="errors.email"
              type="email"
              class="h-10 mt-1"
            />
            <ErrorMessage
              class="text-xs mt-0.5"
              :error="errors.email"
            />
          </div>
        </div>
        <Button class="mt-4 w-full h-10"> Submit </Button>
        <div class="text-end mt-6">
          <RouterLink
            class="text-[#0921D9] text-xs font-semibold"
            to="/login"
          >
            Come back
          </RouterLink>
        </div>
      </form>
    </div>
    <div class="flex-1 relative max-md:hidden">
      <img
        class="absolute top-0 left-0 w-full h-full object-cover rounded-3xl"
        src="@/assets/img/auth-bg.png"
        alt=""
      />
    </div>
  </div>
</template>
<style scoped>
.form-shadow {
  box-shadow:
    0px 1px 3px 0px rgba(16, 24, 40, 0.1),
    0px 1px 2px 0px rgba(16, 24, 40, 0.06);
}
</style> -->
<template>
  <div class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center p-4 overflow-hidden">
    <div class="relative w-full max-w-3xl bg-white shadow-lg rounded-lg overflow-hidden">
      <button @click="$emit('close')" class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-2xl">
        &times;
      </button>
      <div class="flex w-full h-auto">
        <div class="hidden md:block flex-1 bg-cover bg-center" style="background-image: url('/assets/images/bg.jpg');">
          <div class="h-full flex justify-center items-center bg-white/70 p-6">
            <h1 class="text-4xl font-bold text-center">
              <span class="text-blue-500">Elite</span>
              <span class="text-gray-600">Booking</span>
            </h1>
          </div>
        </div>
        <div class="flex-1 flex flex-col justify-center items-center bg-white p-14">
          <h2 class="text-3xl font-semibold mb-6">Forgot Password</h2>
          <form class="w-full max-w-md space-y-5">
            <div>
              <label class="block text-lg font-bold mb-1">Email</label>
              <input type="email" placeholder="Enter your email" required
                class="w-full p-4 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400" />
            </div>
            <button type="submit"
              class="w-full p-5 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition">
              Send Reset Link
            </button>
            <p class="text-center mt-5">
              Remember your password? <a href="#" class="text-blue-500 hover:underline"><RouterLink to="/signin">Login</RouterLink></a>
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['close']);
</script>
