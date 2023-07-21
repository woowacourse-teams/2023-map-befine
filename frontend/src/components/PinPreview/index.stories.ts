import { StoryObj, Meta } from '@storybook/react';
import PinPreview from '.'

const meta = {
  title: 'Components/PinPreview',
  component: PinPreview,
  tags: ['components'],
} as Meta<typeof PinPreview>;

export default meta;
type Story = StoryObj<typeof PinPreview>;

export const PinPreviewComponent: Story = {
  args: {
    pinTitle: '오또상스시',
    pinLocation: '서울특별시 선릉 테헤란로 192-46',
    pinInformation: '초밥을 파는 곳입니다. 점심 특선 있고 초밥 질이 괜찮습니다. 가격대도 다른 곳에 비해서 양호한 편이고 적당히 생각날...',
  }
}




