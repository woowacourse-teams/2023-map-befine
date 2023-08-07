export default interface ToastProps {
  show: boolean;
  message: string;
  type: 'info' | 'warning' | 'error';
}
