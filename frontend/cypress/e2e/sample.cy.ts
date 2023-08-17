describe('메인 페이지', () => {
  beforeEach(() => {
    cy.visit('/');
  });

  it('로고와 소개 문구가 보인다.', () => {
    cy.contains('인기 급상승할 지도').should('be.visible');
  });
});
