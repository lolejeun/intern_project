/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;

import static dao.DAOUtils.fermeturesSilencieuses;
import static dao.DAOUtils.initialisationRequetePreparee;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Parcours;


/**
 *
 * @author lolal
 */

/**
 TO DO
 * map parcours
 * use UserDao and QUestionnaireDao 
 * update 
 * delete
 */
public class ParcoursDao implements DAOInterface<Parcours>{
    
    private DAOFactory daoFactory;

    public ParcoursDao(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }  
    
    private static final String SQL_SELECT_ALL = "SELECT * FROM parcours, user, questionnaire WHERE parcours.user_id = user.id_user AND parcours.user_id = questionnaire.createur_id ";
    private static final String SQL_SELECT_BY_SUBJECT = "SELECT * FROM parcours, questionnaire, user WHERE parcours.user_id = ? AND parcours.user_id = user.id_user AND parcours.user_id = questionnaire.createur_id ";
    private static final String SQL_INSERT = "INSERT INTO parcours (user_id, questionnaire_id, duration) VALUES (?,?,?)";
    private static final String SQL_UPDATE_ALL = "";
    private static final String SQL_SOFT_DELETE = "";   
    private static final String SQL_INSERT_PARCOURS_QUESTION = "INSERT INTO parcours_question (parcours_id, question_id, response_id) VALUES (?,?,?)";
    
    /* Parcours lié à un utlisateur */
    private static final String SQL_PARCOURS_USER = "SELECT * FROM parcours, user, questionnaire, company WHERE parcours.user_id = ? AND parcours.user_id = user.id_user AND parcours.questionnaire_id = questionnaire.id_questionnaire AND user.company = company.matriculation";
    private static final String SQL_COUNT_GOOD_ANSWERS = "SELECT COUNT(*) AS total FROM parcours_question, response WHERE parcours_question.parcours_id = ? AND parcours_question.response_id = response.id_response AND response.validity = 1";
    private static final String SQL_COUNT_ANSWERS = "SELECT COUNT(*) AS total FROM parcours_question WHERE parcours_question.parcours_id = ?";
    
    
    @Override
    public List<Parcours> index() throws DAOException {
        List<Parcours> parcours = new ArrayList();
        
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        //User user = null;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_ALL, false);
            resultSet = preparedStatement.executeQuery();
            
            ResultSetMetaData metadata = resultSet.getMetaData();
            int numberOfColumns = metadata.getColumnCount();
            while (resultSet.next()) {              
                parcours.add(map(resultSet));
            }
            //Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                System.out.println(resultSet);
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParcoursDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        
        return parcours;
    }

    @Override
    public int create(Parcours parcours) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int new_id = 0;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT, true,parcours.getUser_id().getId_user(), parcours.getQuestionnaire_id().getId_questionnaire(),
                    parcours.getDuration());
            int status = preparedStatement.executeUpdate();
            ResultSet value = preparedStatement.getGeneratedKeys();
            if ( value.next() ) {
                new_id = value.getInt(1);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParcoursDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return new_id;
        
    }

    @Override
    public Parcours show(int id) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Parcours questionnaire = null;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_SELECT_BY_SUBJECT, false, id );
            resultSet = preparedStatement.executeQuery();
            //Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                questionnaire = map( resultSet );
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParcoursDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        
        return questionnaire;   
    }

    @Override
    public void delete(int i) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    private static Parcours map(ResultSet resultSet) throws SQLException{
        Parcours parcours = new Parcours();
        parcours.setId(resultSet.getInt("id_parcours"));
        parcours.setDuration(resultSet.getInt("duration"));
        parcours.setQuestionnaire_id(QuestionnaireDao.map(resultSet));
        parcours.setUser_id(UserDao.map(resultSet));
        return parcours;
    }

    @Override
    public void update(int i, Parcours t) throws DAOException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public List<Parcours> indexForUser(int user_id) throws DAOException {
        List<Parcours> parcours = new ArrayList();
        
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_PARCOURS_USER, false, user_id);
            resultSet = preparedStatement.executeQuery();
            
            while (resultSet.next()) {              
                parcours.add(map(resultSet));
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParcoursDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        
        return parcours;
    }
    
    public int countAnswers (int parcours_id){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_COUNT_ANSWERS, false, parcours_id );
            resultSet = preparedStatement.executeQuery();
            //Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt("total");
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }
    
    public int countGoodAnswers (int parcours_id){
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_COUNT_GOOD_ANSWERS, false, parcours_id );
            resultSet = preparedStatement.executeQuery();
            //Parcours de la ligne de données de l'éventuel ResulSet retourné */
            if ( resultSet.next() ) {
                return resultSet.getInt("total");
            }
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return 0;
    }
    
    public int insertParcoursQuestion(int parcours_id, int question_id, int response_id) throws DAOException {
        Connection connexion = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int new_id = 0;
        
        try {
            /* Récupération d'une connexion depuis la Factory */
            connexion = DAOFactory.getConnection();
            preparedStatement = initialisationRequetePreparee( connexion, SQL_INSERT_PARCOURS_QUESTION, true, parcours_id, question_id, response_id);
            int status = preparedStatement.executeUpdate();
            ResultSet value = preparedStatement.getGeneratedKeys();
            if ( value.next() ) {
                new_id = value.getInt(1);
            }
            
        } catch ( SQLException e ) {
            throw new DAOException( e );
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(ParcoursDao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            fermeturesSilencieuses( resultSet, preparedStatement, connexion );
        }
        return new_id;
        
    }
    
}
