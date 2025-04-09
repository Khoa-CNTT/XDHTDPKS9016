<template>
  <div
    class="fixed inset-0 bg-gray-600 bg-opacity-50 flex justify-center items-center p-4 overflow-hidden"
  >
    <div class="relative w-full max-w-3xl bg-white shadow-lg rounded-lg overflow-hidden">
      <button
        @click="$emit('close')"
        class="absolute top-4 right-4 text-gray-500 hover:text-gray-700 text-2xl"
      >
        &times;
      </button>

      <div class="flex w-full h-auto">
        <div
          class="hidden md:block flex-1 bg-cover bg-center"
          style="background-image: url('/assets/images/bg.jpg')"
        >
          <div class="h-full flex justify-center items-center bg-white/70 p-6">
            <h1 class="text-4xl font-bold text-center">
              <span class="text-blue-500">Elite</span>

              <span class="text-gray-600">Booking</span>
            </h1>
          </div>
        </div>

        <div class="flex-1 flex flex-col justify-center items-center bg-white p-14">
          <h2 class="text-3xl font-semibold mb-6">Reset Password</h2>

          <form class="w-full max-w-md space-y-5">
            <div>
              <label class="block text-lg font-bold mb-1">New Password</label>

              <input
                type="password"
                placeholder="Enter new password"
                required
                class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
              />
            </div>

            <div>
              <label class="block text-lg font-bold mb-1">Confirm Password</label>

              <input
                type="password"
                placeholder="Confirm new password"
                required
                class="w-full p-3 border border-gray-300 rounded focus:outline-none focus:ring-2 focus:ring-blue-400"
              />
            </div>

            <button
              type="submit"
              class="w-full p-3 bg-blue-500 text-white rounded text-lg hover:bg-blue-600 transition"
            >
              Reset Password
            </button>

            <p class="text-center mt-5">
              Remember your password?
              <a
                href="#"
                class="text-blue-500 hover:underline"
                ><RouterLink to="/signin">Login</RouterLink></a
              >
            </p>
          </form>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineEmits(['close'])
</script>
