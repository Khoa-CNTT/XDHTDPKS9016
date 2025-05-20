<template>
    <div class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
      <div class="bg-white p-6 rounded shadow-lg w-[90%] max-w-md">
        <h2 class="text-xl font-semibold mb-4">Xác nhận xóa</h2>
        <p class="mb-6">Bạn có chắc chắn muốn xóa loại phòng này không?</p>
        <div class="flex justify-end gap-4">
          <button class="px-4 py-2 bg-gray-300 rounded" @click="$emit('cancel')">Hủy</button>
          <button class="px-4 py-2 bg-red-500 text-white rounded" @click="$emit('confirm')">Xóa</button>
        </div>
      </div>
    </div>
  </template>
  