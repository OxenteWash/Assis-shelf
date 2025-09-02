import React, { useEffect, useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './loan.reducer';

export const Loan = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const loanList = useAppSelector(state => state.loan.entities);
  const loading = useAppSelector(state => state.loan.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="loan-heading" data-cy="LoanHeading">
        <Translate contentKey="washLibraryApp.loan.home.title">Loans</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="washLibraryApp.loan.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/loan/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="washLibraryApp.loan.home.createLabel">Create new Loan</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {loanList && loanList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="washLibraryApp.loan.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('loanTime')}>
                  <Translate contentKey="washLibraryApp.loan.loanTime">Loan Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('loanTime')} />
                </th>
                <th className="hand" onClick={sort('returnTimeExpected')}>
                  <Translate contentKey="washLibraryApp.loan.returnTimeExpected">Return Time Expected</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('returnTimeExpected')} />
                </th>
                <th className="hand" onClick={sort('realReturnTime')}>
                  <Translate contentKey="washLibraryApp.loan.realReturnTime">Real Return Time</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('realReturnTime')} />
                </th>
                <th>
                  <Translate contentKey="washLibraryApp.loan.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {loanList.map((loan, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/loan/${loan.id}`} color="link" size="sm">
                      {loan.id}
                    </Button>
                  </td>
                  <td>{loan.loanTime ? <TextFormat type="date" value={loan.loanTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {loan.returnTimeExpected ? <TextFormat type="date" value={loan.returnTimeExpected} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{loan.realReturnTime ? <TextFormat type="date" value={loan.realReturnTime} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{loan.user ? loan.user.id : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/loan/${loan.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/loan/${loan.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/loan/${loan.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="washLibraryApp.loan.home.notFound">No Loans found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Loan;
