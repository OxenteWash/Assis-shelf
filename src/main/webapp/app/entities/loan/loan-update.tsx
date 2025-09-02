import React, { useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Col, Row } from 'reactstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { createEntity, getEntity, reset, updateEntity } from './loan.reducer';

export const LoanUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const loanEntity = useAppSelector(state => state.loan.entity);
  const loading = useAppSelector(state => state.loan.loading);
  const updating = useAppSelector(state => state.loan.updating);
  const updateSuccess = useAppSelector(state => state.loan.updateSuccess);

  const handleClose = () => {
    navigate('/loan');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    values.loanTime = convertDateTimeToServer(values.loanTime);
    values.returnTimeExpected = convertDateTimeToServer(values.returnTimeExpected);
    values.realReturnTime = convertDateTimeToServer(values.realReturnTime);

    const entity = {
      ...loanEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          loanTime: displayDefaultDateTime(),
          returnTimeExpected: displayDefaultDateTime(),
          realReturnTime: displayDefaultDateTime(),
        }
      : {
          ...loanEntity,
          loanTime: convertDateTimeFromServer(loanEntity.loanTime),
          returnTimeExpected: convertDateTimeFromServer(loanEntity.returnTimeExpected),
          realReturnTime: convertDateTimeFromServer(loanEntity.realReturnTime),
          user: loanEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="washLibraryApp.loan.home.createOrEditLabel" data-cy="LoanCreateUpdateHeading">
            <Translate contentKey="washLibraryApp.loan.home.createOrEditLabel">Create or edit a Loan</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="loan-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('washLibraryApp.loan.loanTime')}
                id="loan-loanTime"
                name="loanTime"
                data-cy="loanTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('washLibraryApp.loan.returnTimeExpected')}
                id="loan-returnTimeExpected"
                name="returnTimeExpected"
                data-cy="returnTimeExpected"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('washLibraryApp.loan.realReturnTime')}
                id="loan-realReturnTime"
                name="realReturnTime"
                data-cy="realReturnTime"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="loan-user" name="user" data-cy="user" label={translate('washLibraryApp.loan.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/loan" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default LoanUpdate;
