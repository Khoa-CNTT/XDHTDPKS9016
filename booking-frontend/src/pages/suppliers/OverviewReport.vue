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
  <Tabs default-value="account" class="w-full max-w-md mx-auto mt-10">
    <!-- Tab List -->
    <TabsList class="grid grid-cols-2 gap-2 bg-gray-100 p-1 rounded-xl shadow-inner">
      <TabsTrigger
        value="account"
        class="py-2 text-sm font-semibold text-gray-600 rounded-lg transition duration-300 data-[state=active]:bg-white data-[state=active]:text-blue-600"
      >
        Tài khoản
      </TabsTrigger>
      <TabsTrigger
        value="password"
        class="py-2 text-sm font-semibold text-gray-600 rounded-lg transition duration-300 data-[state=active]:bg-white data-[state=active]:text-green-600"
      >
        Đổi mật khẩu
      </TabsTrigger>
    </TabsList>

    <!-- Thông tin tài khoản -->
    <TabsContent value="account">
      <Card class="mt-5 border-none shadow-lg rounded-xl overflow-hidden">
        <CardHeader class="bg-blue-500 text-white text-center py-4">
          <CardTitle class="text-lg font-bold">Thông tin tài khoản</CardTitle>
          <CardDescription class="text-sm text-white/80 mt-1">Xem và cập nhật thông tin cá nhân của bạn.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-4 p-6 bg-white">
          <div>
            <Label for="name">Tên</Label>
            <Input id="name" value="Nguyễn Văn A" disabled class="bg-gray-100 rounded-md mt-1" />
          </div>
          <div>
            <Label for="email">Email</Label>
            <Input id="email" value="nhacungcap@gmail.com" disabled class="bg-gray-100 rounded-md mt-1" />
          </div>
          <div>
            <Label for="password">Mật khẩu</Label>
            <Input id="password" value="********" disabled class="bg-gray-100 rounded-md mt-1" />
            <Button variant="link" class="text-blue-600 px-0 mt-1">Đổi mật khẩu</Button>
          </div>
        </CardContent>
        <CardFooter class="bg-gray-50 px-6 py-4">
          <Button class="w-full bg-blue-600 hover:bg-blue-700 text-white">Cập nhật thông tin</Button>
        </CardFooter>
      </Card>
    </TabsContent>

    <!-- Đổi mật khẩu -->
    <TabsContent value="password">
      <Card class="mt-5 border-none shadow-lg rounded-xl overflow-hidden">
        <CardHeader class="bg-green-500 text-white text-center py-4">
          <CardTitle class="text-lg font-bold">Đổi mật khẩu</CardTitle>
          <CardDescription class="text-sm text-white/80 mt-1">Thay đổi mật khẩu hiện tại của bạn.</CardDescription>
        </CardHeader>
        <CardContent class="space-y-4 p-6 bg-white">
          <div>
            <Label for="current">Mật khẩu hiện tại</Label>
            <Input type="password" id="current" placeholder="Nhập mật khẩu cũ" class="bg-gray-100 rounded-md mt-1" />
          </div>
          <div>
            <Label for="new">Mật khẩu mới</Label>
            <Input type="password" id="new" placeholder="Nhập mật khẩu mới" class="bg-gray-100 rounded-md mt-1" />
          </div>
        </CardContent>
        <CardFooter class="bg-gray-50 px-6 py-4">
          <Button class="w-full bg-green-600 hover:bg-green-700 text-white">Lưu mật khẩu</Button>
        </CardFooter>
      </Card>
    </TabsContent>
  </Tabs>
</template>
