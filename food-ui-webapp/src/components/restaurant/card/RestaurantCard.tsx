import { makeStyles } from '@material-ui/core/styles';
import { red } from '@material-ui/core/colors';

import Typography from '@material-ui/core/Typography';
import Avatar from '@material-ui/core/Avatar';
import CardHeader from '@material-ui/core/CardHeader';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardMedia from '@material-ui/core/CardMedia';
import IconButton from '@material-ui/core/IconButton';
import MoreVertIcon from '@material-ui/icons/MoreVert';
import CardContent from '@material-ui/core/CardContent';

import { useSelector } from 'react-redux';
import { useHistory } from 'react-router-dom';

import Restaurant from '../../../domain/Restaurant';
import { RootState } from '../../../gateway';

const useStyles = makeStyles((theme) => ({
  root: {
    maxWidth: 345,
  },
  media: {
    height: 0,
    paddingTop: '56.25%', // 16:9
  },
  avatar: {
    backgroundColor: red[500],
  },
}));

type Props = {
  uuid: string,
  name: string,
  logo: string, 
  description: string,
  address: string
}

type RestaurantState = {
  restaurants: Restaurant[], 
  loading: boolean
}

const RestaurantCard = ({uuid, name, logo, description, address}: Props) => {

  const classes = useStyles();

  const { loading, restaurants }: RestaurantState = useSelector(
    (state: RootState) => state.restaurant
  );

  let history = useHistory();

  return (
    <div>
      <Card className={classes.root}>
        <CardActionArea onClick={(evt) => {
            history.push(`/order/${uuid}`);
          }}
        >
          <CardHeader
            avatar={
              <Avatar aria-label="recipe" className={classes.avatar}>
                PH
              </Avatar>
            }
            action={
              <IconButton aria-label="settings">
                <MoreVertIcon />
              </IconButton>
            }
            title={ name }
            subheader={ address }
          />
          <CardMedia
            className={classes.media}
            image={ logo }
            title={ name }
          />
          <CardContent>
            <Typography variant="body2" color="textSecondary" component="p">
              { description }
            </Typography>
          </CardContent>
        </CardActionArea>
      </Card>
    </div>
  );
}

export default RestaurantCard;