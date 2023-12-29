describe('메인 페이지', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('로고와 다양한 지도 소개 글이 보인다.', () => {
    cy.get('[data-cy="logo"]').should('be.visible');

    cy.contains('인기 급상승할 지도').should('be.visible');
  });

  it('인기 급상승할 지도 전체보기 버튼을 클릭한다.', () => {
    cy.contains('전체 보기').click();

    cy.contains('인기 급상승할 지도').should('be.visible');
  });

  it('인기 급상승할 지도의 첫번째 토픽을 클릭해 토픽 상세 페이지가 보인다.', () => {
    cy.get('[data-cy="topic-card"]')
      .children()
      .each(($el, index) => {
        if (index === 0) $el.click();
      });

    cy.get('[data-cy="topic-info"]').should('be.visible');
  });
});

describe('토픽 상세 페이지', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('토픽 상세 페이지에서 핀 체크박스를 클릭하면 뽑아오기 기능이 활성화 된다.', () => {
    cy.get('[data-cy="topic-card"]')
      .children()
      .each(($el, index) => {
        if (index === 0) $el.click();
      });

    cy.get('input[type="checkbox"]').each(($el, index) => {
      if (index === 0) $el.click();
    });

    cy.contains('뽑아오기').should('be.visible');
  });

  it('활성화된 핀 뽑아오기 기능 중 취소하기 버튼 누르면 기능 비활성화 되도록 한다.', () => {
    cy.get('[data-cy="topic-card"]')
      .children()
      .each(($el, index) => {
        if (index === 0) $el.click();
      });

    cy.get('input[type="checkbox"]').each(($el, index) => {
      if (index === 0) $el.click();
    });

    cy.contains('취소하기').click();
    cy.contains('뽑아오기').should('not.be.visible');
  });

  it('토픽 상세 페이지에서 핀을 클릭하면 핀 상세 페이지가 나온다.', () => {
    cy.get('[data-cy="topic-card"]')
      .children()
      .each(($el, index) => {
        if (index === 0) $el.click();
      });

    cy.wait(5000);

    cy.get('span').each(($el, index) => {
      if (index === 6) $el.click();
    });

    cy.get('[data-cy="pin-detail"]').scrollTo('bottom');

    cy.contains('내 지도에 저장하기').should('be.visible');
  });

  it('핀 상세 페이지에서 내 지도에 저장하기 버튼 누르면 토스트 메시지가 나온다.', () => {
    cy.get('[data-cy="topic-card"]')
      .children()
      .each(($el, index) => {
        if (index === 0) $el.click();
      });

    cy.wait(5000);

    cy.get('span').each(($el, index) => {
      if (index === 6) $el.click();
    });

    cy.get('[data-cy="pin-detail"]').scrollTo('bottom');

    cy.contains('내 지도에 저장하기').click();

    cy.contains('로그인 후 사용해주세요.').should('be.visible');
  });
});
