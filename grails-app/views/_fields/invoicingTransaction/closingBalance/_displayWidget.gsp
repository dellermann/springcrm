<span class="still-unpaid-${
    (bean?.closingBalance <=> BigDecimal.ZERO) < 0 ? 'unpaid'
      : ((bean?.closingBalance <=> BigDecimal.ZERO) > 0 ? 'too-much' : 'paid')
  }"><g:formatCurrency number="${bean?.closingBalance}" displayZero="true"
    external="true"
/></span>
