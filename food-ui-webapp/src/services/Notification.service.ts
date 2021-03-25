import { store } from 'react-notifications-component';

export const showWarning = (message: string, title='Aviso') => {
  store.addNotification({
    title,
    message,
    type: "warning",
    insert: "top",
    container: "top-right",
    animationIn: ["animate__animated", "animate__fadeIn"],
    animationOut: ["animate__animated", "animate__fadeOut"],
    dismiss: {
      duration: 0,
      showIcon: true
    }
  });
};

export const showError = (message: string, title='Erro') => {
  store.addNotification({
    title,
    message,
    type: "danger",
    insert: "top",
    container: "top-right",
    animationIn: ["animate__animated", "animate__fadeIn"],
    animationOut: ["animate__animated", "animate__fadeOut"],
    dismiss: {
      duration: 0,
      showIcon: true
    }
  });
};

export const showSuccess = (message: string, title= 'Sucesso') => {
  store.addNotification({
    title,
    message,
    type: "success",
    insert: "top",
    container: "top-right",
    animationIn: ["animate__animated", "animate__fadeIn"],
    animationOut: ["animate__animated", "animate__fadeOut"],
    dismiss: {
      duration: 3000,
      showIcon: true
    }
  });
};
