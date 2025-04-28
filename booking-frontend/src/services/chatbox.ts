import { $api } from '@/api/ofetch'

interface ChatAiPayload {
  prompt: string
}

interface ChatAiResponse {
  response: string
}

export const chatAiApi = async (payload: ChatAiPayload): Promise<ChatAiResponse> => {
  return $api('/chat/ai', {
    method: 'POST',
    body: payload,
  })
}