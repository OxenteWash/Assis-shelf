import loan from 'app/entities/loan/loan.reducer';
import book from 'app/entities/book/book.reducer';
import category from 'app/entities/category/category.reducer';
import reservation from 'app/entities/reservation/reservation.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  loan,
  book,
  category,
  reservation,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
