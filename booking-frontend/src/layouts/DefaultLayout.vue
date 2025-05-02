<script setup lang="ts">
import { ref } from 'vue'
import Header from '@/components/layout/Header.vue'
import Footer from '@/components/layout/Footer.vue'
import { chatAiApi } from '@/services/chatbox'

const showChat = ref(false)
const chatMessage = ref('')
const chatHistory = ref<{ from: 'user' | 'bot'; message: string }[]>([])

function toggleChat() {
  showChat.value = !showChat.value
}

const sendChat = async () => {
  if (!chatMessage.value.trim()) return

  // Thêm tin nhắn người dùng vào lịch sử
  chatHistory.value.push({ from: 'user', message: chatMessage.value })

  const currentMessage = chatMessage.value
  chatMessage.value = ''

  try {
    const res = await chatAiApi({ prompt: currentMessage })
    chatHistory.value.push({ from: 'bot', message: res.response })
  } catch (error) {
    chatHistory.value.push({ from: 'bot', message: 'Đã xảy ra lỗi. Vui lòng thử lại sau.' })
    console.error(error)
  }
}
</script>

<template>
  <div class="container-div">
    <Header class="header" />
    <div class="content">
      <slot />
      <!-- Nút bật chat -->
      <div class="fixed bottom-6 right-6 z-50">
        <button @click="toggleChat"
          class="bg-blue-600 text-white p-5 rounded-full shadow-lg hover:bg-blue-500 transition-all duration-300 animate-shake">
          <Icon icon="lucide:message-circle" class="h-8 w-8" />
        </button>
      </div>

      <!-- Cửa sổ chat -->
      <transition name="fade-slide">
        <div v-if="showChat"
          class="fixed bottom-20 right-6 w-96 bg-white shadow-2xl rounded-lg border border-gray-300 z-50 flex flex-col max-h-[80vh]">
          <!-- Header -->
          <div class="p-4 border-b font-semibold bg-blue-600 text-white rounded-t-lg flex justify-between items-center">
            <span>Chat với chúng tôi</span>
            <button @click="toggleChat" class="text-white text-xl font-bold">✕</button>
          </div>

          <!-- Nội dung chat -->
          <div class="p-4 flex-1 overflow-y-auto space-y-3 text-sm text-gray-800 bg-gray-50">
            <div v-for="(msg, index) in chatHistory" :key="index" class="flex px-2"
              :class="msg.from === 'user' ? 'justify-end' : 'justify-start'">
              <div :class="msg.from === 'user' ? 'bg-blue-100 text-blue-800' : 'bg-gray-200 text-gray-800'"
                class="px-4 py-2 rounded-lg max-w-[75%] whitespace-pre-line leading-relaxed break-words shadow">
                {{ msg.message }}
              </div>
            </div>
          </div>
          <!-- Input & gửi -->
          <div class="p-3 border-t flex items-center gap-2">
            <input type="text" v-model="chatMessage" placeholder="Nhập tin nhắn..."
              class="flex-1 border rounded px-3 py-2 text-sm focus:outline-none" @keyup.enter="sendChat" />
            <button @click="sendChat"
              class="bg-blue-600 hover:bg-blue-500 text-white px-4 py-2 rounded text-sm transition">
              Gửi
            </button>
          </div>
        </div>
      </transition>
    </div>
    <Footer class="footer" />
  </div>
</template>

<style scoped>
.container-div {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  width: 100%;
  overflow: hidden;
}

.header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.content {
  flex: 1;
  margin-top: 80px;
  padding: 0px;
  overflow-y: auto;
}

.footer {
  width: 100%;
  margin-top: auto;
}
</style>
