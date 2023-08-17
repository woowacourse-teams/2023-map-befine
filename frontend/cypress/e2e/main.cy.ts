describe('메인 페이지', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('로고와 다양한 지도 소개 글이 보인다.', () => {
    cy.get('div[aria-label="괜찮을지도 로고 및 홈으로 이동 버튼"]').should(
      'be.visible',
    );

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
