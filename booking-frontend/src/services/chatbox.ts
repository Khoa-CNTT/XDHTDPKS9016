import { $api } from '@/api/ofetch'

interface ChatAiPayload {
  prompt: string
}

interface ChatAiResponse {
  response: string
}

export const chatAiApi = async (payload: ChatAiPayload): Promise<ChatAiResponse> => {
  return $api('http://103.69.97.39:8080/api/v1/chat/ai', {
    method: 'POST',
    body: payload,
  })
}