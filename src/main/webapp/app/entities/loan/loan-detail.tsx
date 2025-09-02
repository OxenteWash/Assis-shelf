import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './loan.reducer';

export const LoanDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const loanEntity = useAppSelector(state => state.loan.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="loanDetailsHeading">
          <Translate contentKey="washLibraryApp.loan.detail.title">Loan</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{loanEntity.id}</dd>
          <dt>
            <span id="loanTime">
              <Translate contentKey="washLibraryApp.loan.loanTime">Loan Time</Translate>
            </span>
          </dt>
          <dd>{loanEntity.loanTime ? <TextFormat value={loanEntity.loanTime} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="returnTimeExpected">
              <Translate contentKey="washLibraryApp.loan.returnTimeExpected">Return Time Expected</Translate>
            </span>
          </dt>
          <dd>
            {loanEntity.returnTimeExpected ? (
              <TextFormat value={loanEntity.returnTimeExpected} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="realReturnTime">
              <Translate contentKey="washLibraryApp.loan.realReturnTime">Real Return Time</Translate>
            </span>
          </dt>
          <dd>
            {loanEntity.realReturnTime ? <TextFormat value={loanEntity.realReturnTime} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <Translate contentKey="washLibraryApp.loan.user">User</Translate>
          </dt>
          <dd>{loanEntity.user ? loanEntity.user.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/loan" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/loan/${loanEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LoanDetail;
