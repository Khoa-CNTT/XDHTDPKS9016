import { i18n } from '@/plugins/i18n'
const t = (val: string) => {
  return i18n.global.t(val)
}

// Kiểm tra email
function validEmail(email: string) {
  if (email == '' || email == null || email == undefined) {
    return {
      mess: t('validation.em_blank'),
      check: false,
    }
  } else {
    const re =
      /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

    // Kiểm tra định dạng email
    if (!re.test(email)) {
      return {
        mess: t('validation.em_format'),
        check: false,
      }
    } else {
      return {
        mess: '',
        check: true,
      }
    }
  }
}

// Kiểm tra username
function validUsername(username: string) {
  if (username == '' || username == null || username == undefined) {
    return {
      mess: t('validation.username_blank'),
      check: false,
    }
  } else {
    const re = /^[a-zA-Z0-9]{8,32}$/; // username chỉ chứa chữ thường và số, không có khoảng trắng
    if (!re.test(username)) {
      return {
        mess: t('validation.username_format'),
        check: false,
      }
    } else {
      return {
        mess: '',
        check: true,
      }
    }
  }
}

// Kiểm tra password
function validPass(pass: string) {
  if (pass == '' || pass == null || pass == undefined) {
    return {
      mess: t('validation.ps_blank'),
      check: false,
    }
  } else {
    // password phải chứa chữ hoa, chữ thường, số, ký tự đặc biệt, không có khoảng trắng, từ 8 đến 32 ký tự
   const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*,;()\[\]{}])[A-Za-z\d!@#$%^&*,;()\[\]{}]{8,32}$/;
    if (!re.test(pass)) {
      return {
        mess: t('validation.ps_format'),
        check: false,
      }
    } else {
      return {
        mess: '',
        check: true,
      }
    }
  }
}

// Kiểm tra confirm password
function validPassConfirm(confirmPass: string, pass: string) {
  if (confirmPass === '' || confirmPass == null || confirmPass == undefined) {
    return {
      mess: t('validation.ps_confirm_blank'),
      check: false,
    }
  } else if (confirmPass !== pass) {
    return {
      mess: t('validation.ps_confirm_match'),
      check: false,
    }
  } else {
    return {
      mess: '',
      check: true,
    }
  }
}

export { validEmail, validUsername, validPass, validPassConfirm }
