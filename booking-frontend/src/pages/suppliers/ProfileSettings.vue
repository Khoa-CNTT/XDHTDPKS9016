<script setup lang="ts">
import { Button } from '@/components/ui/button'
import {
  Card,
  CardContent,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from '@/components/ui/card'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@/components/ui/tabs'
</script>

<template>
  <Tabs default-value="account" class="w-[400px] mx-auto mt-10">
    <!-- Tab List -->
    <TabsList class="grid w-full grid-cols-2 gap-2">
      <TabsTrigger value="account" class="text-lg font-medium py-3 border-b-2 border-transparent hover:border-blue-500 transition duration-300 rounded-t-lg">
        Thông tin tài khoản
      </TabsTrigger>
      <TabsTrigger value="password" class="text-lg font-medium py-3 border-b-2 border-transparent hover:border-blue-500 transition duration-300 rounded-t-lg">
        Đổi mật khẩu
      </TabsTrigger>
    </TabsList>

    <!-- Tab Content: Account Information -->
    <TabsContent value="account">
      <Card class="mt-4 shadow-2xl hover:shadow-3xl transform transition-all duration-500 rounded-lg overflow-hidden border border-gray-300 hover:scale-105 hover:translate-y-2">
        <CardHeader class="bg-gradient-to-r from-blue-100 to-blue-300 text-center p-4">
          <CardTitle class="text-xl font-semibold text-gray-700">Thông tin tài khoản</CardTitle>
          <CardDescription class="text-sm text-gray-600">Cập nhật thông tin tài khoản của bạn tại đây.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-4 p-6 bg-white">
          <div class="space-y-2">
            <Label for="name" class="text-gray-600">Tên</Label>
            <Input id="name" value="Nguyễn Văn A" disabled class="text-gray-600 bg-gray-100 rounded-lg p-2" />
          </div>
          <div class="space-y-2">
            <Label for="email" class="text-gray-600">Email</Label>
            <Input id="email" value="nhacungcap@gmail.com" disabled class="text-gray-600 bg-gray-100 rounded-lg p-2" />
          </div>
          <div class="space-y-2">
            <Label for="password" class="text-gray-600">Mật khẩu</Label>
            <Input id="password" value="********" disabled class="text-gray-600 bg-gray-100 rounded-lg p-2" />
            <Button class="text-blue-600 mt-2 py-1 px-2 bg-transparent hover:underline rounded-lg">Đổi mật khẩu</Button>
          </div>
        </CardContent>
        <CardFooter class="bg-gray-50">
          <Button class="w-full py-3 bg-blue-600 text-white rounded-lg hover:bg-blue-700 transition duration-300">Cập nhật thông tin</Button>
        </CardFooter>
      </Card>
    </TabsContent>

    <!-- Tab Content: Change Password -->
    <TabsContent value="password">
      <Card class="mt-4 shadow-2xl hover:shadow-3xl transform transition-all duration-500 rounded-lg overflow-hidden border border-gray-300 hover:scale-105 hover:translate-y-2">
        <CardHeader class="bg-gradient-to-r from-green-100 to-green-300 text-center p-4">
          <CardTitle class="text-xl font-semibold text-gray-700">Đổi mật khẩu</CardTitle>
          <CardDescription class="text-sm text-gray-600">Thay đổi mật khẩu của bạn tại đây. Sau khi thay đổi, bạn sẽ bị đăng xuất.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-4 p-6 bg-white">
          <div class="space-y-2">
            <Label for="current" class="text-gray-600">Mật khẩu hiện tại</Label>
            <Input id="current" type="password" placeholder="Nhập mật khẩu hiện tại" class="text-gray-600 bg-gray-100 rounded-lg p-2" />
          </div>
          <div class="space-y-2">
            <Label for="new" class="text-gray-600">Mật khẩu mới</Label>
            <Input id="new" type="password" placeholder="Nhập mật khẩu mới" class="text-gray-600 bg-gray-100 rounded-lg p-2" />
          </div>
        </CardContent>
        <CardFooter class="bg-gray-50">
          <Button class="w-full py-3 bg-green-600 text-white rounded-lg hover:bg-green-700 transition duration-300">Lưu mật khẩu</Button>
        </CardFooter>
      </Card>
    </TabsContent>
  </Tabs>
</template>
