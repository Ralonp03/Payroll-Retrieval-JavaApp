//////////////////////////////////////Apartado 3
            //Eliminar todas las nóminas y trabajadores, excepto aquellas nóminas y trabajadores que pertenezcan a la misma empresa que el trabajador introducido. 
        
            consultaHQL = "FROM Trabajadorbbdd t ";
            query = sesion.createQuery(consultaHQL);
            List<Trabajadorbbdd> listaResultado3 = query.list();
            int aux = 0, aux1 = 0;

                for(Trabajadorbbdd tb:listaResultado3)
                {
                    if(tb.getEmpresas().getIdEmpresa()!= empresa)
                    {
                        aux1 = tb.getIdTrabajador();
                        tx = sesion.beginTransaction();
                        for (Iterator<Nomina> it = tb.getNominas().iterator(); it.hasNext(); ) 
                        {
                            Nomina n = it.next();
                            if (n.getTrabajadorbbdd().equals(tb))
                            {
                                aux = n.getIdNomina();
                                String HQLborrado = "DELETE Nomina nn WHERE nn.idNomina =:param1";
                                query = sesion.createQuery(HQLborrado);
                                query.setParameter("param1", aux);
                                query.executeUpdate();
                                sesion.saveOrUpdate(tb);
                            }
                        }
                        String HQLborrado = "DELETE Trabajadorbbdd tb WHERE tb.idTrabajador =:param2";
                        query = sesion.createQuery(HQLborrado);
                        query.setParameter("param2", aux1);
                        query.executeUpdate();
                        sesion.saveOrUpdate(tb);
                        tx.commit();
                    }
                }
