<template>
  <div class="wrapper">
    <nav class="main-header navbar navbar-expand navbar-white navbar-light">
      <ul class="navbar-nav">
        <li class="nav-item">
          <a class="nav-link" data-widget="pushmenu" href="#"><i class="fas fa-bars"></i></a>
        </li>
      </ul>
    </nav>

    <aside class="main-sidebar sidebar-dark-primary elevation-4">
      <a href="#" class="brand-link">
        <span class="brand-text font-weight-light">AdminLTE Vue</span>
      </a>
      <div class="sidebar">
        <nav class="mt-2">
          <ul class="nav nav-pills nav-sidebar flex-column">
            <li class="nav-item">
              <router-link to="/admin/dashboard" class="nav-link">
                <i class="nav-icon fas fa-tachometer-alt"></i>
                <p>Dashboard</p>
              </router-link>
            </li>
          </ul>
        </nav>
      </div>
    </aside>

    <div class="content-wrapper p-3">
      <router-view />
    </div>
  </div>
</template>

<script setup>
</script>

<style>
@import url('https://cdn.jsdelivr.net/npm/@fortawesome/fontawesome-free@6.5.1/css/all.min.css');
@import url('https://cdn.jsdelivr.net/npm/admin-lte@3.2/dist/css/adminlte.min.css');
</style>

<script>
const loadScript = (src) => {
  const script = document.createElement('script')
  script.src = src
  script.async = true
  document.body.appendChild(script)
}

loadScript('https://cdn.jsdelivr.net/npm/jquery@3.6.4/dist/jquery.min.js')
loadScript('https://cdn.jsdelivr.net/npm/bootstrap@4.6.2/dist/js/bootstrap.bundle.min.js')
loadScript('https://cdn.jsdelivr.net/npm/admin-lte@3.2/dist/js/adminlte.min.js')
</script>
