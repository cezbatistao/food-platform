import { Theme, createStyles, makeStyles, useTheme } from '@material-ui/core/styles';

import Typography from '@material-ui/core/Typography';
import Card from '@material-ui/core/Card';
import CardActionArea from '@material-ui/core/CardActionArea';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';

const useStyles = makeStyles((theme: Theme) =>
  createStyles({
    root: {
      display: 'flex',
    },
    container: {
      paddingTop: theme.spacing(4),
      paddingBottom: theme.spacing(4),
    },
    details: {
      display: 'flex',
      flexDirection: 'column',
    },
    content: {
      flex: '1 0 auto',
    },
    cover: {
      width: 151,
    },
  }),
);

type Props = {
  uuid: string,
  name: string,
  description: string,
  value: string
}

const RestaurantItem = ({uuid, name, description, value}: Props) => {
  
  const classes = useStyles();
  const theme = useTheme();

  const formatNumber = (valueStr: string) => {
    var value: number = +valueStr;
    return value.toLocaleString('pt-br', {style: 'currency', currency: 'BRL'});
  }
  
  return (
    <>
      <Card key={ uuid } className={classes.root}>
        <CardMedia
          className={classes.cover}
          image="https://astron.com.br/wp-content/uploads/2017/07/pizza-site-or.jpg"
          title={ name }
        />
        <CardActionArea>
          <div className={classes.details}>
            <CardContent className={classes.content}>
              <Typography component="h5" variant="h5">
                { name }
              </Typography>
              <Typography variant="subtitle1" color="textSecondary">
                { description }
              </Typography>
              <Typography variant="subtitle2" color="textSecondary">
                { formatNumber(value) }
              </Typography>
            </CardContent>
          </div>
        </CardActionArea>
      </Card>
      <br />
    </>
  );
}

export default RestaurantItem;