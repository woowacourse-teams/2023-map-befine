import React, { Component, ErrorInfo, ReactNode } from 'react';

interface Props {
  children?: ReactNode;
  fallback: React.ElementType;
}

interface State {
  hasError: boolean;
  info: Error | null;
}

class ErrorBoundary extends Component<Props, State> {
  public state: State = {
    hasError: false,
    info: null,
  };

  public static getDerivedStateFromError(error: Error): State {
    return { hasError: true, info: error };
  }

  public componentDidCatch(error: Error, errorInfo: ErrorInfo) {
    console.error('Uncaught error:', error, errorInfo);
  }

  public render() {
    const { hasError, info } = this.state;
    const { children } = this.props;
    if (hasError) {
      return <this.props.fallback error={info} />;
    }

    return children;
  }
}

export default ErrorBoundary;
