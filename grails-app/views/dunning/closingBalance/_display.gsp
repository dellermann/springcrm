<g:applyLayout name="fieldDisplay">
  <span class="still-unpaid-${bean?.closingBalance < 0 ? 'unpaid' : (bean?.closingBalance > 0 ? 'too-much' : 'paid')}"><g:formatCurrency number="${bean?.closingBalance}" displayZero="true" external="true" /></span>
</g:applyLayout>
