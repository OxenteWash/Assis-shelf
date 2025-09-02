import './home.scss';

import React from 'react';
import { Link } from 'react-router-dom';
import { Card, CardText, CardImgOverlay, CardImg, Col, Row, Input, Label, Container } from 'reactstrap';

import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);

  return (
    <Container>
      <Row md={16}>
        <Col>My Books</Col>
        <Col>Categories</Col>
        <Col>Popular lists</Col>
        <Col>
          <Input type={'text'} placeholder={'Search'} />
        </Col>
        {account.login ? (
          <>
            <Col md={2}>
              <Label for={'login'}>Login:</Label>
              <Input id={'login'} type={'text'} placeholder={'Login'} />
            </Col>
            <Col md={2}>
              <Label for={'password'}> Password:</Label>
              <Input id={'password'} type={'password'} placeholder={'password'} />
            </Col>
          </>
        ) : (
          <></>
        )}
        <Col>Log in</Col>
        <Col>Sign Up</Col>
      </Row>
      <Row>
        <Row>
          <h3>Welcome to Cosmology</h3>
          <span>There is nothing worse than giving the longest of legs to the smallest of ideas - ASSIS, De Machado</span>
        </Row>
        <Row>
          <span>Last Readed Books</span>
        </Row>
        <Row>
          <span>Top trending Books</span>
        </Row>
        <Row>
          <span>Classical</span>
        </Row>
      </Row>
    </Container>
  );
};

export default Home;
